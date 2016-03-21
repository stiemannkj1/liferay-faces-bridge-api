/**
 * Copyright (c) 2000-2016 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.liferay.faces.bridge.context;

import javax.faces.FacesWrapper;
import javax.faces.context.ResponseWriter;
import javax.portlet.PortletResponse;


/**
 * @author  Neil Griffin
 */
public abstract class HeadResponseWriterFactory implements FacesWrapper<HeadResponseWriterFactory> {

	public abstract HeadResponseWriter getHeadResponseWriter(ResponseWriter responseWriter,
		PortletResponse portletResponse);
}
