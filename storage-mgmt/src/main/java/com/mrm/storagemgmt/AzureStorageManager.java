package com.mrm.storagemgmt;

import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.UserDelegationKey;
import com.azure.storage.blob.sas.BlobSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;

import java.time.OffsetDateTime;

public class AzureStorageManager {
        public final BlobServiceClient blobServiceClient;
        public final DefaultAzureCredential defaultAzureCredential;
        public final String storageAccountName;

        public AzureStorageManager(String storageAccountName) {
                // Instantiate a DefaultAzureCredential - this will use the context of the local
                // user or the managed identity/workload identity of the service it is deployed
                // to in order to authenticate to the target Azure Storage service.
                // NOTE: Best practice for best performance is for this object to be a singleton
                // and ideally is wrapped in a bean and injected into the constructor
                defaultAzureCredential = new DefaultAzureCredentialBuilder().build();

                // Create a BlobServiceClient using the DefaultAzureCredential.
                // NOTE: For best performance, this serivce client object should also be a
                // singleton and perist for the lifetime of the application
                blobServiceClient = new BlobServiceClientBuilder()
                                .endpoint(String.format("https://%s.blob.core.windows.net/", storageAccountName))
                                .credential(defaultAzureCredential)
                                .buildClient();

                // Set the storage account name. This allows this class to be scoped against a
                // single storage account
                this.storageAccountName = storageAccountName;
        }

        public String getBlobDownloadUrl(String containerName, String blobName) {

                // Use the blob service client to get an instance of a blob container client
                // scoped to a specific blob
                BlobClient blobContainerClient = blobServiceClient
                                .getBlobContainerClient(containerName)
                                .getBlobClient(blobName);

                // Create a user delegation SAS token for the blob
                String sasToken = createUserDelegationSASBlob(blobContainerClient);

                // Compose the final download URL for the blob including the SAS token
                String endpoint = String.format("https://%s.blob.core.windows.net/%s/%s?%s", storageAccountName,
                                containerName, blobName, sasToken);

                return endpoint;
        }

        private String createUserDelegationSASBlob(BlobClient blobClient) {

                // This is the raw duration for expiry time. This should be refactored to be a
                // configuration or environment variable and should be set a value higher than
                // the time it would take the lab agents to complete the maximum amount of 
                // retries they are configured for.
                int configuredExpiryTime = 5;

                // This date time offset is the duration that both the user delegation key and
                // the SAS token are valid for.
                OffsetDateTime expiryTimeInMinutes = OffsetDateTime.now().plusMinutes(configuredExpiryTime);

                // Setting the SAS token to only allow read access to the specific blob in
                // question.
                BlobSasPermission blobSasPermission = new BlobSasPermission().setReadPermission(true);

                BlobServiceSasSignatureValues blobServiceSasSignatureValues = new BlobServiceSasSignatureValues(
                                expiryTimeInMinutes, blobSasPermission);
                
                // Use the blob service client to get a user delegation key for use with the
                // blob container client
                UserDelegationKey userDelegationKey = blobServiceClient
                                .getUserDelegationKey(OffsetDateTime.now().minusMinutes(configuredExpiryTime),
                                                expiryTimeInMinutes);

                // Use the user delegation key to generate a SAS token for the blob
                String sasToken = blobClient.generateUserDelegationSas(blobServiceSasSignatureValues,
                                userDelegationKey);

                return sasToken;
        }
}