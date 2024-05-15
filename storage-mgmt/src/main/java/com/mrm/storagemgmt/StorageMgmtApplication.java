package com.mrm.storagemgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StorageMgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(StorageMgmtApplication.class, args);

		String storageAccountName = "";
		String containerName = "";
		String blobName = "";

		//Comment this section out and hardcode the values above if desired
		if(args.length > 2) {
			storageAccountName = args[0];
			containerName = args[1];
			blobName = args[2];
		} else {
			System.out.println("Usage: java -jar storage-mgmt.jar <storageAccountName> <containerName> <blobName>");
			System.exit(1);
		}

		//Create an instance of the custom azure storage manager class.  If possible this should be instantiated
		//as a singleton via dependency injection.
		AzureStorageManager azureStorageManager = new AzureStorageManager(storageAccountName);

		//getBlobDownloadUrl will return a URL specific to the passed in blob/file name that will have a
		//timed SAS token for read only access to just the given file
		String url = azureStorageManager.getBlobDownloadUrl(containerName,
				blobName);

		System.out.println(String.format("Download URL with SAS Token: %s", url));

		//This uses the same pattern as the Azure Storage Manager - a custom class uses the DefaultAzureCredential
		//object (which wraps the underlying Workload Identity/Managed Identity) to make calls to the 
		//target Key Vault without having tp specify an API key, etc.
		
		//AzureKeyVaultManager azureKeyVaultManager = new AzureKeyVaultManager("yourKeyVaultName");
		//String secretValue = azureKeyVaultManager.getSecret("nameOfYourSecret");
		//System.out.println(secretValue);		
	}
}
