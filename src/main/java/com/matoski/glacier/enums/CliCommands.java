package com.matoski.glacier.enums;

public enum CliCommands {

    Help("help"),
    ListVaults("list-vaults"),
    CreateVault("create-vault"),
    DeleteVault("delete-vault"),
    ListVaultJobs("list-vault-jobs"),
    VaultJobInfo("vault-job-info"),
    InventoryRetrieve("inventory-retrieve"),
    InventoryDownload("inventory-download"),
    ListJournal("list-journal"),
    InitDownload("init-download"),
    DownloadArchive("download-archive"),
    DeleteArchive("delete-archive"),
    UploadArchive("upload-archive"),
    ListMultipartUploads("list-multipart-uploads"),
    MultipartUploadInfo("multipart-upload-info"),
    AbortMultipartUpload("abort-multipart-upload"),
    Sync("sync"),
    Purge("purge");

    public static CliCommands from(String x) {
	for (CliCommands currentType : CliCommands.values()) {
	    if (x.equals(currentType.getPropertyName())) {
		return currentType;
	    }
	}
	// default command
	return CliCommands.Help;
    }

    private String propertyName;

    CliCommands(String propName) {
	this.propertyName = propName;
    }

    public String getPropertyName() {
	return propertyName;
    }

}
