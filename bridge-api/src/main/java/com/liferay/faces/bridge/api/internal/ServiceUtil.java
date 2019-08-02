/**
 * Copyright (c) 2000-2019 Liferay, Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liferay.faces.bridge.api.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

import javax.faces.FacesException;


/**
 * @author  kylestiemann
 */
public final class ServiceUtil {

	private ServiceUtil() {
		throw new AssertionError();
	}

	public static <T> T createServiceInstance(Class<T> clazz) {

		T t = null;
		ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz);

		if (serviceLoader != null) {

			Iterator<T> iterator = serviceLoader.iterator();

			while ((t == null) && iterator.hasNext()) {
				t = iterator.next();
			}
		}

		// In an OSGi environment, SPI-Fly can be flaky, so attempt to obtain the services from the current JAR in
		// case we are inside a Faces uber JAR.
		if (t == null) {

			InputStream inputStream = clazz.getResourceAsStream("/META-INF/services/" + clazz.getName());

			if (inputStream != null) {

				Scanner scanner = null;

				try {

					scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\n");

					String serviceClassName = scanner.next();

					if (serviceClassName != null) {

						serviceClassName = serviceClassName.trim();

						ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

						if (classLoader == null) {
							classLoader = clazz.getClassLoader();
						}

						Class<?> serviceImplClass = Class.forName(serviceClassName, true, classLoader);
						t = (T) serviceImplClass.newInstance();
					}
				}
				catch (Throwable throwable) {
					throw new ServiceConfigurationError("Failed to obtain service for " + clazz.getName() +
						" due to the following error:", throwable);
				}
				finally {

					if (scanner != null) {
						scanner.close();
					}

					try {
						inputStream.close();
					}
					catch (IOException e) {
						// Do nothing.
					}
				}
			}
		}

		if (t == null) {
			throw new FacesException("Unable locate service for " + clazz.getName());
		}

		return t;
	}
}
