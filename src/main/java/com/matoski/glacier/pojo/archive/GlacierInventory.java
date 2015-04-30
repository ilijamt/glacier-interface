package com.matoski.glacier.pojo.archive;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Amazon Glacier Inventory Metadata.
 * <p/>
 * <p>
 * An object that holds all the information from the inventory, we use this to convert to any needed
 * journal
 * </p>
 *
 * @author Ilija Matoski (ilijamt@gmail.com)
 */
public class GlacierInventory {

    /**
     * The vault arn.
     */
    @SerializedName(value = "VaultARN")
    private String vaultArn;
    /**
     * The inventory date.
     */
    @SerializedName(value = "InventoryDate")
    private String inventoryDate;
    /**
     * The list of all the archives.
     */
    @SerializedName(value = "ArchiveList")
    private List<ArchiveItem> archiveList;

    /**
     * Get the archive list.
     *
     * @return {@link #archiveList}
     */
    public List<ArchiveItem> getArchiveList() {
        return archiveList;
    }

    /**
     * Set archive list.
     *
     * @param archiveList the archiveList to set
     */
    public void setArchiveList(List<ArchiveItem> archiveList) {
        this.archiveList = archiveList;
    }

    /**
     * Get the inventory date.
     *
     * @return {@link #inventoryDate}
     */
    public String getInventoryDate() {
        return inventoryDate;
    }

    /**
     * Set the inventory date.
     *
     * @param inventoryDate the inventoryDate to set
     */
    public void setInventoryDate(String inventoryDate) {
        this.inventoryDate = inventoryDate;
    }

    /**
     * Get the vault ARN.
     *
     * @return {@link #vaultArn}
     */
    public String getVaultArn() {
        return vaultArn;
    }

    /**
     * Set the vault ARN.
     *
     * @param vaultArn the vaultARN to set
     */
    public void setVaultArn(String vaultArn) {
        this.vaultArn = vaultArn;
    }

    /**
     * Description of an item in the archive.
     *
     * @author Ilija Matoski (ilijamt@gmail.com)
     */
    public static class ArchiveItem {

        /**
         * The ID of the archive.
         */
        @SerializedName(value = "ArchiveId")
        private String archiveId;

        /**
         * The description of the archive.
         */
        @SerializedName(value = "ArchiveDescription")
        private String archiveDescription;

        /**
         * The creation date of the archive.
         */
        @SerializedName(value = "CreationDate")
        private String creationDate;

        /**
         * The size of the archive.
         */
        @SerializedName(value = "Size")
        private long size;

        /**
         * The hash of the archive.
         */
        @SerializedName(value = "SHA256TreeHash")
        private String sha256TreeHash;

        /**
         * Get the archive description.
         *
         * @return {@link #archiveDescription}
         */
        public String getArchiveDescription() {
            return this.archiveDescription;
        }

        /**
         * Set the archive description.
         *
         * @param archiveDescription the archiveDescription to set
         */
        public void setArchiveDescription(String archiveDescription) {
            this.archiveDescription = archiveDescription;
        }

        /**
         * Get the archive id.
         *
         * @return {@link #archiveId}
         */
        public String getArchiveId() {
            return this.archiveId;
        }

        /**
         * Set the archive id.
         *
         * @param archiveId the archiveId to set
         */
        public void setArchiveId(String archiveId) {
            this.archiveId = archiveId;
        }

        /**
         * Get the creation date.
         *
         * @return {@link #creationDate}
         */
        public String getCreationDate() {
            return this.creationDate;
        }

        /**
         * Set the creation date.
         *
         * @param creationDate the creationDate to set
         */
        public void setCreationDate(String creationDate) {
            this.creationDate = creationDate;
        }

        /**
         * Get the SHA256 tree hash.
         *
         * @return {@link #sha256TreeHash}
         */
        public String getSha256TreeHash() {
            return this.sha256TreeHash;
        }

        /**
         * Set the SHA256 Tree Hash.
         *
         * @param hash the sHA256TreeHash to set
         */
        public void setSha256TreeHash(String hash) {
            this.sha256TreeHash = hash;
        }

        /**
         * Get the archive size.
         *
         * @return {@link #size}
         */
        public long getSize() {
            return size;
        }

        /**
         * Set the size of the archive.
         *
         * @param size the size to set
         */
        public void setSize(long size) {
            this.size = size;
        }

    }

}
