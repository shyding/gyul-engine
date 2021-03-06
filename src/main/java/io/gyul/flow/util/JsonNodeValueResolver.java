/**
 * Copyright © 2019 The Project-gyul Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gyul.flow.util;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BigIntegerNode;
import com.fasterxml.jackson.databind.node.BinaryNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DecimalNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.jknack.handlebars.ValueResolver;

/**
 * JsonNodeValueResolver
 *  
 * @author sungjae
 */
public enum JsonNodeValueResolver implements ValueResolver {

	/**
	 * The singleton instance.
	 */
	INSTANCE;

	@Override
	public Object resolve(final Object context, final String name) {
		Object value = null;
		if (context instanceof ArrayNode) {
			try {
				if (name.equals("length")) {
					return ((ArrayNode) context).size();
				}
				value = resolve(((ArrayNode) context).get(Integer.parseInt(name)));
			} catch (NumberFormatException ex) {
				// ignore undefined key and move on, see
				// https://github.com/jknack/handlebars.java/pull/280
				value = null;
			}
		} else if (context instanceof JsonNode) {
			value = resolve(((JsonNode) context).get(name));
		}
		return value == null ? UNRESOLVED : value;
	}

	@Override
	public Object resolve(final Object context) {
		if (context instanceof JsonNode) {
			return resolve((JsonNode) context);
		}
		return UNRESOLVED;
	}

	/**
	 * Resolve a {@link JsonNode} object to a primitive value.
	 *
	 * @param node
	 *            A {@link JsonNode} object.
	 * @return A primitive value, json object, json array or null.
	 */
	private static Object resolve(final JsonNode node) {
		// binary node
		if (node instanceof BinaryNode) {
			return ((BinaryNode) node).asText();
		}
		// boolean node
		if (node instanceof BooleanNode) {
			return ((BooleanNode) node).booleanValue();
		}
		// null node
		if (node instanceof NullNode) {
			return null;
		}
		// numeric nodes
		if (node instanceof BigIntegerNode) {
			return ((BigIntegerNode) node).bigIntegerValue();
		}
		if (node instanceof DecimalNode) {
			return ((DecimalNode) node).decimalValue();
		}
		if (node instanceof DoubleNode) {
			return ((DoubleNode) node).doubleValue();
		}
		if (node instanceof IntNode) {
			return ((IntNode) node).intValue();
		}
		if (node instanceof LongNode) {
			return ((LongNode) node).longValue();
		}
		// pojo
		if (node instanceof POJONode) {
			return ((POJONode) node).getPojo();
		}
		// string
		if (node instanceof TextNode) {
			return ((TextNode) node).textValue();
		}
		// object node to map
		if (node instanceof ObjectNode) {
			return (ObjectNode) node;
		}
		// container, array or null
		return node;
	}

	@Override
	public Set<Entry<String, Object>> propertySet(final Object context) {
		if (context instanceof ObjectNode) {
			ObjectNode node = (ObjectNode) context;
			Iterator<String> fieldNames = node.fieldNames();
			Map<String, Object> result = new LinkedHashMap<String, Object>();
			while (fieldNames.hasNext()) {
				String key = fieldNames.next();
				result.put(key, resolve(node, key));
			}
			return result.entrySet();
		}
		return Collections.emptySet();
	}

}
