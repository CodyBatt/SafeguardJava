/*
Copyright (c) Microsoft Open Technologies, Inc.
All Rights Reserved
See License.txt in the project root for license information.
*/

package microsoft.aspnet.signalr.client;

import microsoft.aspnet.signalr.client.http.HttpConnection;

public interface PlatformComponent {
    /**
     * Returns a platform-specific HttpConnection
     * @param logger Logger
     * @return HttpConnection Http Connection
     */
    public HttpConnection createHttpConnection(Logger logger);

    /**
     * Returns a platform-specific HttpConnection 
     * @param logger Logger
     * @param clientCertificatePath Client certificate
     * @param clientCertificatePassword Client certificate password
     * @param clientCertificateAlias Client certificate alias
     * @param ignoreSsl Ignore SSL certificate verification
     * @return HttpConnection Http Connection
     */
    public HttpConnection createHttpConnection(Logger logger, String clientCertificatePath, 
            char[] clientCertificatePassword, String clientCertificateAlias, boolean ignoreSsl);
    
    /**
     * Returns a platform-specific Operating System name
     * @return String OS Name
     */

    public String getOSName();
}
