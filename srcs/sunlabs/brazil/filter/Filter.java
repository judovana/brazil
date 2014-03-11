/*
 * Filter.java
 *
 * Brazil project web application toolkit,
 * export version: 2.3 
 * Copyright (c) 1999-2004 Sun Microsystems, Inc.
 *
 * Sun Public License Notice
 *
 * The contents of this file are subject to the Sun Public License Version 
 * 1.0 (the "License"). You may not use this file except in compliance with 
 * the License. A copy of the License is included as the file "license.terms",
 * and also available at http://www.sun.com/
 * 
 * The Original Code is from:
 *    Brazil project web application toolkit release 2.3.
 * The Initial Developer of the Original Code is: suhler.
 * Portions created by suhler are Copyright (C) Sun Microsystems, Inc.
 * All Rights Reserved.
 * 
 * Contributor(s): cstevens, suhler.
 *
 * Version:  2.2
 * Created by suhler on 99/07/29
 * Last modified by suhler on 04/11/30 15:19:38
 *
 * Version Histories:
 *
 * 2.2 04/11/30-15:19:38 (suhler)
 *   fixed sccs version string
 *
 * 2.1 02/10/01-16:38:59 (suhler)
 *   version change
 *
 * 1.8 00/05/31-13:43:20 (suhler)
 *   fix docs
 *
 * 1.7 00/05/22-14:02:54 (suhler)
 *   doc updates
 *
 * 1.6 00/04/24-12:56:45 (cstevens)
 *   doc
 *
 * 1.5 00/03/10-16:56:31 (cstevens)
 *   API documentation and documenting pending issues
 *
 * 1.4 99/10/06-12:29:00 (suhler)
 *   use MimeHeaders
 *
 * 1.3 99/08/30-09:36:52 (suhler)
 *   remove wildcarded imports
 *
 * 1.2 99/08/06-08:30:20 (suhler)
 *   make filter interface a subclass of handler, so filters can access the
 *   request object before the content-to-be-filtered is fetched.
 *
 * 1.2 99/07/29-16:17:19 (Codemgr)
 *   SunPro Code Manager data about conflicts, renames, etc...
 *   Name history : 2 1 filter/Filter.java
 *   Name history : 1 0 tail/Filter.java
 *
 * 1.1 99/07/29-16:17:18 (suhler)
 *   date and time created 99/07/29 16:17:18 by suhler
 *
 */

package sunlabs.brazil.filter;

import sunlabs.brazil.server.Handler;
import sunlabs.brazil.server.Request;
import sunlabs.brazil.util.http.MimeHeaders;

/**
 * The <code>Filter</code> interface is used by the
 * {@link FilterHandler}
 * to examine and dynamically rewrite the contents of web pages obtained from
 * some source before returning that page to the client.
 * <p>
 * A chain of filters can be established in the manner of a pipeline.  The
 * <code>FilterHandler</code> sends the output of a <code>Filter</code>
 * to the input of the next <code>Filter</code>.
 * <p>
 * The <code>init</code> and <code>respond</code> methods inherited from
 * the <code>Handler</code> interface are called as for ordinary handlers:
 * <dl>
 * <dt> {@link Handler#init}
 * <dd> is called when the server starts, to obtain run-time
 *	configuration information.
 *
 * <dt> {@link Handler#respond}
 * <dd>	is called when the request comes in, before the request is sent to
 *	the wrapped handler.  This method returns true to indicate that the
 *	request has been completely handled by this <code>Filter</code>, and
 *	no further processing filtering takes place.
 * </dl>
 *
 * @author	Stephen Uhler (stephen.uhler@sun.com)
 * @author	Colin Stevens (colin.stevens@sun.com)
 * @version		2.2
 */

public interface Filter
    extends Handler
{
    /**
     * Gives this <code>Filter</code> the chance to examine the HTTP
     * response headers from the wrapped <code>Handler</code>, before the
     * content has been retrieved.
     * <p>
     * If this <code>Filter</code> <b>does</b> want to examine and possibly
     * rewrite the content, it should return <code>true</code>; once the
     * content is available, the <code>filter</code> method will be invoked.
     * For instance, if this <code>Filter</code> is only interested in
     * rewriting "text/html" pages, it should return <code>false</code> if
     * the "Content-Type" is "image/jpeg".
     * If all filters return <code>false</code> for the
     * <code>shouldFilter</code> method, the {@link FilterHandler} can
     * switch to a more effient mechanism of delivering content to the client.
     * <p>
     * The MIME headers may also be modified by this <code>Filter</code>,
     * for instance, to change the "Content-Type" of a web page.  The
     * "Content-Length" will automatically be computed.
     *
     * @param	request
     *		The in-progress HTTP request.
     *
     * @param	headers
     *		The MIME headers generated by the wrapped <code>Handler</code>.
     *
     * @return	<code>true</code> if this filter would like to examine and
     *		possibly rewrite the content, <code>false</code> otherwise.
     */
    public boolean
    shouldFilter(Request request, MimeHeaders headers);

    /**
     * Filters the content generated by the wrapped <code>Handler</code>.
     * The content may be arbitrarily rewritten by this method.
     * <p>
     * The MIME headers may also be modified by this <code>Filter</code>,
     * for instance, to change the "Content-Type" of a web page.
     * The "Content-Length" will automatically be computed by the
     * <code>FilterHandler</code>.
     *
     * @param	request
     *		The finished HTTP request.
     *
     * @param	headers
     *		The MIME headers generated by the <code>Handler</code>.
     *
     * @param	content
     *		The output from the <code>Handler</code> that this
     *		<code>Filter</code> may rewrite.
     *
     * @return	The rewritten content.  The <code>Filter</code> may return
     *		the original <code>content</code> unchanged.  The
     *		<code>Filter</code> may return <code>null</code> to indicate
     *		that the <code>FilterHandler</code> should stop processing the
     *		request and should not return any content to the client.
     */
    public byte[]
    filter(Request request, MimeHeaders headers, byte[] content);
}