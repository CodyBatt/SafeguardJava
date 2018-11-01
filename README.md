[![GitHub](https://img.shields.io/github/license/OneIdentity/SafeguardDotNet.svg)](https://github.com/OneIdentity/SafeguardDotNet/blob/master/LICENSE)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.oneidentity.safeguard/safeguardjava/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.oneidentity.safeguard/safeguard)

# SafeguardJava

One Identity Safeguard Web API Java SDK

## Introduction

All functionality in Safeguard is available via the Safeguard API. There is
nothing that can be done in the Safeguard UI that cannot also be performed
using the Safeguard API programmatically.

SafeguardJava is provided to facilitate calling the Safeguard API from Java.
It is meant to remove the complexity of dealing with authentication via
Safeguard's embedded secure token service (STS). It also facilitates
authentication using client certificates, which is the recommended
authentication mechanism for automated processes. The basic usage is to call
`Connect()` to establish a connection to Safeguard, then you can call
`InvokeMethod()` multiple times using the same authenticated connection.

SafeguardJava also provides an easy way to call Safeguard A2A from Java. The
A2A service requires client certificate authentication for retrieving passwords
for application integration. When Safeguard A2A is properly configured,
specified passwords can be retrieved with a single method call without
requiring access request workflow approvals. Safeguard A2A is protected by
API keys and IP restrictions in addition to client certificate authentication.

SafeguardJava includes an SDK for listening to Safeguard's powerful, real-time
event notification system. Safeguard provides role-based event notifications
via SignalR to subscribed clients. If a Safeguard user is an Asset Administrator
events related to the creation, modification, or deletion of Assets and Asset
Accounts will be sent to that user. When used with a certificate user, this
provides an opportunity for reacting programmatically to any data modification
in Safeguard. Events are also supported for access request workflow and for
A2A password changes.

## Getting Started

A simple code example for calling the Safeguard API:

```Java
byte[] password = GetPasswordSomehow(); // default password is "Admin123"
ISafeguardConnection connection = Safeguard.Connect("safeguard.sample.corp", "local", "Admin", password, null, true);
System.out.println(connection.InvokeMethod(Service.Core, Method.Get, "Me", null, null, null));
connection.dispose();
```

Certificates must be in a PFX (PKCS12) file.

```Java
byte[] certificatePassword = GetPasswordSomehow();
ISafeguardConnection connection = Safeguard.Connect("safeguard.sample.corp", "C:\\cert.pfx", certificatePassword, null, true);
System.out.println(connection.InvokeMethod(Service.Core, Method.Get, "Me", null, null, null));
connection.dispose();
```

A final authentication method that is available is using an existing Safeguard API token.

```Java
byte[] apiToken = GetTokenSomehow();
ISafeguardConnection connection = Safeguard.Connect("safeguard.sample.corp", apiToken, null, true);
System.out.println(connection.InvokeMethod(Service.Core, Method.Get, "Me", null, null, null));
connection.dispose();
```

Calling the simple 'Me' endpoint provides information about the currently logged
on user.

## About the Safeguard API

The Safeguard API is a REST-based Web API. Safeguard API endpoints are called
using HTTP operators and JSON (or XML) requests and responses. The Safeguard API
is documented using Swagger. You may use Swagger UI to call the API directly or
to read the documentation about URLs, parameters, and payloads.

To access the Swagger UI use a browser to navigate to:
`https://<address>/service/<service>/swagger`

- `<address>` = Safeguard network address
- `<service>` = Safeguard service to use

The Safeguard API is made up of multiple services: core, appliance, notification,
and a2a.

|Service|Description|
|-|-|
|core|Most product functionality is found here. All cluster-wide operations: access request workflow, asset management, policy management, etc.|
|appliance|Appliance specific operations, such as setting IP address, maintenance, backups, support bundles, appliance management|
|notification|Anonymous, unauthenticated operations. This service is available even when the appliance isn't fully online|
|a2a|Application integration specific operations. Fetching passwords, making access requests on behalf of users, etc.|

Each of these services provides a separate Swagger endpoint.

You may use the `Authorize` button at the top of the screen to get an API token
to call the Safeguard API directly using Swagger.

To call the a2a service you should begin by using `Safeguard.A2A.GetContext()` rather than
`Safeguard.Connect()`.

### Examples

Most functionality is in the core service as mentioned above.  The notification service
provides read-only information for status, etc.

#### Anonymous Call for Safeguard Status

```Java
ISafeguardConnection connection = Safeguard.Connect("safeguard.sample.corp");
System.out.println(connection.InvokeMethod(Service.Core, Method.Get, "Status"));
```

#### Create a New Linux Asset

```Java
// Assume connection is already made
String json = connection.InvokeMethod(Service.Core, Method.Post, "Assets", 
    JsonConvert.SerializeObject(new { 
        Name = "linux.blue.vas",
        NetworkAddress = "linux.blue.vas",
        Description = "A new linux asset",
        PlatformId = 188, // Ubuntu Other
        AssetPartitionId = -1
    }));
System.out.println(json);
```

## Using SafeguardJava from a New NetBeans Project

First, create a directory with the name you want to give your project and change directory into it.

Run:
```PowerShell
PS> dotnet new console  - Replace with an archetype call
```

This will create a `console` project.  

Run:
```PowerShell
PS> dotnet add package OneIdentity.SafeguardJava    - Replace with adding to the POM.XML
```

This will add the latest OneIdentity.SafeguardJava Maven package into your project.

Run:
```PowerShell
PS> dotnet restore   - Replace with Maven
```

This will restore NuGet packages into your project so you can get code completion in the editor

Finally, run:
```PowerShell
PS> code .
```

This will open the Visual Studio Code editor so you can begin adding code to your project.

Add the using directive at the top your file to call SafeguardJava:
```Java
Import OneIdentity.SafeguardDotNet;   - Replace with import statements
```
