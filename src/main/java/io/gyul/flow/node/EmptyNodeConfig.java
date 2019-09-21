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
package io.gyul.flow.node;

import lombok.NoArgsConstructor;

/**
 * EmptyNodeConfig
 * 
 * @author sungjae
 */
@NoArgsConstructor
public class EmptyNodeConfig {
	private static final EmptyNodeConfig c = new EmptyNodeConfig();
	
	@SuppressWarnings("unused")
	private int _ignore = 0;	//Meaningless fields to prevent json conversion errors

	public static EmptyNodeConfig instance() {
		return c;
	}
}