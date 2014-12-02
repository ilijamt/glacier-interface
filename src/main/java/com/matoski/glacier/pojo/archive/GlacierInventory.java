package com.matoski.glacier.pojo.archive;

import java.util.List;

/**
 * Amazon Glacier Inventory Metadata.
 * 
 * <p>
 * An object that holds all the information from the inventory, we use this to convert to any needed
 * journal
 * </p>
 * 
 * @author Ilija Matoski (ilijamt@gmail.com)
 *
 */
public class GlacierInventory {

  /**
   * Description of an item in the archive.
   * 
   * @author Ilija Matoski (ilijamt@gmail.com)
   *
   */
  public static class ArchiveItem {

    /**
     * The ID of the archive.
     */
    private String archiveId;

    /**
     * The description of the archive.
     */
    private String archiveDescription;

    /**
     * The creation date of the archive.
     */
    private String creationDate;

    /**
     * The size of the archive.
     */
    private long size;

    /**
     * The hash of the archive.
     */
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
     * Get the archive id.
     * 
     * @return {@link #archiveId}
     */
    public String getArchiveId() {
      return this.archiveId;
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
     * Get the SHA256 tree hash.
     * 
     * @return {@link #sha256TreeHash}
     */
    public String getSha256TreeHash() {
      return this.sha256TreeHash;
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
     * Set the archive description.
     * 
     * @param archiveDescription
     *          the archiveDescription to set
     */
    public void setArchiveDescription(String archiveDescription) {
      this.archiveDescription = archiveDescription;
    }

    /**
     * Set the archive id.
     * 
     * @param archiveId
     *          the archiveId to set
     */
    public void setArchiveId(String archiveId) {
      this.archiveId = archiveId;
    }

    /**
     * Set the creation date.
     * 
     * @param creationDate
     *          the creationDate to set
     */
    public void setCreationDate(String creationDate) {
      this.creationDate = creationDate;
    }

    /**
     * Set the SHA256 Tree Hash.
     * 
     * @param hash
     *          the sHA256TreeHash to set
     */
    public void setSha256TreeHash(String hash) {
      this.sha256TreeHash = hash;
    }

    /**
     * Set the size of the archive.
     * 
     * @param size
     *          the size to set
     */
    public void setSize(long size) {
      this.size = size;
    }

  }

  /**
   * The vault arn.
   */
  private String vaultArn;

  /**
   * The inventory date.
   */
  private String inventoryDate;

  /**
   * The list of all the archives.
   */
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
   * Get the inventory date.
   * 
   * @return {@link #inventoryDate}
   */
  public String getInventoryDate() {
    return inventoryDate;
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
   * Set archive list.
   * 
   * @param archiveList
   *          the archiveList to set
   */
  public void setArchiveList(List<ArchiveItem> archiveList) {
    this.archiveList = archiveList;
  }

  /**
   * Set the inventory date.
   * 
   * @param inventoryDate
   *          the inventoryDate to set
   */
  public void setInventoryDate(String inventoryDate) {
    this.inventoryDate = inventoryDate;
  }

  /**
   * Set the vault ARN.
   * 
   * @param vaultArn
   *          the vaultARN to set
   */
  public void setVaultArn(String vaultArn) {
    this.vaultArn = vaultArn;
  }

}
