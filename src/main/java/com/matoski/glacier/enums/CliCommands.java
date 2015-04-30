package com.matoski.glacier.enums;

/**
 * Contains a list of command, including their command line counterparts.
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public enum CliCommands {

    Version("version"),

    /**
     * Triggers the help command.
     */
    Help("help"),

    /**
     * Triggers the list vaults command.
     */
    ListVaults("list-vaults"),
    /**
     * Triggers the create vault command.
     */
    CreateVault("create-vault"),

    /**
     * Triggers the delete vault command.
     */
    DeleteVault("delete-vault"),

    /**
     * Triggers the list vault jobs command.
     */
    ListVaultJobs("list-vault-jobs"),

    /**
     * Triggers the vault job info command.
     */
    VaultJobInfo("vault-job-info"),

    /**
     * Triggers the inventory retrieve command.
     */
    InventoryRetrieve("inventory-retrieve"),

    /**
     * Triggers the inventory download command.
     */
    InventoryDownload("inventory-download"),

    /**
     * Triggers the list journal command.
     */
    ListJournal("list-journal"),

    /**
     * Triggers the init download command.
     */
    InitDownload("init-download"),

    /**
     * Triggers the download job command.
     */
    DownloadJob("download-job"),

    /**
     * Triggers the delete archive.
     */
    DeleteArchive("delete-archive"),

    /**
     * Triggers the upload archive command.
     */
    UploadArchive("upload-archive"),

    /**
     * Triggers the list multipart uploads command.
     */
    ListMultipartUploads("list-multipart-uploads"),

    /**
     * Triggers the multipart upload info command.
     */
    MultipartUploadInfo("multipart-upload-info"),

    /**
     * Triggers the abort multipart upload.
     */
    AbortMultipartUpload("abort-multipart-upload"),

    /**
     * Triggers the sync command.
     */
    Sync("sync"),

    /**
     * Triggers the purge vault command.
     */
    PurgeVault("purge-vault"),

    /**
     * Triggers the verify journal command.
     */
    VerifyJournal("verify-journal");

    /**
     * The string representation of the command.
     */
    private String propertyName;

    /**
     * Constructor.
     *
     * @param propName string representation of the command
     */
    CliCommands(String propName) {
        this.propertyName = propName;
    }

    /**
     * Create a {@link CliCommands} from the string representation of the command.
     *
     * @param command The string representation to search for
     * @return The correct representation, if not found returns default {@link #Help}
     */
    public static CliCommands from(String command) {
        for (CliCommands currentType : CliCommands.values()) {
            if (command.equals(currentType.getPropertyName())) {
                return currentType;
            }
        }
        // default command
        return CliCommands.Help;
    }

    /**
     * Gets the string representation of the command.
     *
     * @return {@link #propertyName}
     */
    public String getPropertyName() {
        return propertyName;
    }

}
