# Requirements To Run Locally:

- You must have access to a storage account that can be routed to from your local environment
   - The storage account should have public networking enabled 
   - *or*
   - The storage account should be enabled from select VNETs that are accessible from VPN or the corporate network
- Your Entra ID account must be assigned the ***Storage Account Contributor*** RBAC role on the storage account
- If you want to test out the Key Vault functionality you also need to be able to route to the target Key Vault and be assigned the ***Key Vault Secrets User*** RBAC role or he granted the appropriate access policies (if your KV is setup for that mode)

# Getting Started

- Create a blob container in the storage account
- Upload a file to the blob container using Azure Storage Manager
- Execute the sample app and pass in the following parameters: _storageAccountName_ (Note: this is just the account name, not the whole URL), _containerName_, _fileName_
- The sample app should emit a fully formed URL that you can use to download the file you uploaded

# Notes

- This sample uses the latest (v4.0) Spring Cloud Azure Dependencies BOM
- Review the included pom.xml file to confirm which dependencies are required

### Reference Documentation
For further reference, please consider the following links:

* [Azure Identity Client Library for Java](https://learn.microsoft.com/en-us/java/api/overview/azure/identity-readme?view=azure-java-stable)
* [Use Microsoft Entra Workload ID with AKS](https://learn.microsoft.com/en-us/azure/aks/workload-identity-overview?tabs=java)
* [User Delegation SAS for a Blob with Java](https://learn.microsoft.com/en-us/azure/storage/blobs/storage-blob-user-delegation-sas-create-java)
* [Spring Cloud Azure Developer Guid](https://learn.microsoft.com/en-us/azure/developer/java/spring-framework/developer-guide-overview)