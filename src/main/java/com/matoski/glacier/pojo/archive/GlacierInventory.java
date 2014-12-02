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
    private String ArchiveId;

    /**
     * The description of the archive.
     */
    private String ArchiveDescription;

    /**
     * The creation date of the archive.
     */
    private String CreationDate;

    /**
     * The size of the archive.
     */
    private long Size;

    /**
     * The hash of the archive.
     */
    private String SHA256TreeHash;

    /**
     * @return the archiveDescription
     */
    public String getArchiveDescription() {
      return ArchiveDescription;
    }

    /**
     * @return the archiveId
     */
    public String getArchiveId() {
      return ArchiveId;
    }

    /**
     * @return the creationDate
     */
    public String getCreationDate() {
      return CreationDate;
    }

    /**
     * @return the sHA256TreeHash
     */
    public String getSHA256TreeHash() {
      return SHA256TreeHash;
    }

    /**
     * @return the size
     */
    public long getSize() {
      return Size;
    }

    /**
     * @param archiveDescription
     *          the archiveDescription to set
     */
    public void setArchiveDescription(String archiveDescription) {
      ArchiveDescription = archiveDescription;
    }

    /**
     * @param archiveId
     *          the archiveId to set
     */
    public void setArchiveId(String archiveId) {
      ArchiveId = archiveId;
    }

    /**
     * @param creationDate
     *          the creationDate to set
     */
    public void setCreationDate(String creationDate) {
      CreationDate = creationDate;
    }

    /**
     * @param sHA256TreeHash
     *          the sHA256TreeHash to set
     */
    public void setSHA256TreeHash(String sHA256TreeHash) {
      SHA256TreeHash = sHA256TreeHash;
    }

    /**
     * @param size
     *          the size to set
     */
    public void setSize(long size) {
      Size = size;
    }

  }

  /**
   * The vault arn
   */
  private String VaultARN;

  /**
   * The inventory date
   */
  private String InventoryDate;

  /**
   * The list of all the archives
   */
  private List<ArchiveItem> ArchiveList;

  /**
   * @return the archiveList
   */
  public List<ArchiveItem> getArchiveList() {
    return ArchiveList;
  }

  /**
   * @return the inventoryDate
   */
  public String getInventoryDate() {
    return InventoryDate;
  }

  /**
   * @return the vaultARN
   */
  public String getVaultARN() {
    return VaultARN;
  }

  /**
   * @param archiveList
   *          the archiveList to set
   */
  public void setArchiveList(List<ArchiveItem> archiveList) {
    ArchiveList = archiveList;
  }

  /**
   * @param inventoryDate
   *          the inventoryDate to set
   */
  public void setInventoryDate(String inventoryDate) {
    InventoryDate = inventoryDate;
  }

  /**
   * @param vaultARN
   *          the vaultARN to set
   */
  public void setVaultARN(String vaultARN) {
    VaultARN = vaultARN;
  }

}
