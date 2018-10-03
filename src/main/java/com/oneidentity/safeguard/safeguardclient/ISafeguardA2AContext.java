package com.oneidentity.safeguard.safeguardclient;

/// <summary>

import com.oneidentity.safeguard.safeguardclient.exceptions.ObjectDisposedException;
import com.oneidentity.safeguard.safeguardclient.exceptions.SafeguardForJavaException;

/// This is a reusable interface for calling Safeguard A2A without having to continually
/// pass the client certificate authentication information.
/// </summary>
public interface ISafeguardA2AContext
{
    /// <summary>
    /// Retrieves a password using Safeguard A2A.
    /// </summary>
    /// <param name="apiKey">API key corresponding to the configured account.</param>
    /// <returns>The password.</returns>
    char[] RetrievePassword(char[] apiKey) throws ObjectDisposedException, SafeguardForJavaException;

    /// <summary>
    /// Gets an A2A event listener. The handler passed in will be registered for the AssetAccountPasswordUpdated
    /// event, which is the only one supported in A2A. You just have to call Start().
    /// </summary>
    /// <param name="apiKey">API key correspondingto the configured account to listen for.</param>
    /// <param name="handler">A delegate to call any time the AssetAccountPasswordUpdate event occurs.</param>
    /// <returns>The event listener.</returns>
//    ISafeguardEventListener GetEventListener(SecureString apiKey, SafeguardEventHandler handler);

    /// <summary>
    /// Gets an A2A event listener. The handler passed in will be registered for the AssetAccountPasswordUpdated
    /// event, which is the only one supported in A2A. You just have to call Start().
    /// </summary>
    /// <param name="apiKey">API key correspondingto the configured account to listen for.</param>
    /// <param name="handler">A delegate to call any time the AssetAccountPasswordUpdate event occurs.</param>
    /// <returns>The event listener.</returns>
//    ISafeguardEventListener GetEventListener(SecureString apiKey, SafeguardParsedEventHandler handler);
    
    public void Dispose();

}
