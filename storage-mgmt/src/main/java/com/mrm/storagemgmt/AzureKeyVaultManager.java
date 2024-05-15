package com.mrm.storagemgmt;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

public class AzureKeyVaultManager {

    private final SecretClient secretClient;

    public AzureKeyVaultManager(String keyVaultName) {
        
        // Create a secret client using managed identity
        secretClient = new SecretClientBuilder()
                .vaultUrl(String.format("https://%s.vault.azure.ne", keyVaultName))
                .credential(new DefaultAzureCredentialBuilder().build())
                .buildClient();
    }

    public String getSecret(String secretName) {

        // Retrieve the secret (storage account key)
        KeyVaultSecret keyVaultSecret = secretClient.getSecret(secretName);
        String value = keyVaultSecret.getValue();

        return value;
    }
}
