package codeu.model.data;

import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.UUID;

/**
 * Class representing media (a picture, video, or sound byte).
 * The media is uploaded by Users.
 */
public class Media {
  private final UUID id;
  private final UUID owner;
  private final Instant creation;
  private final String title;
  private final BufferedImage content;
  private final String contentType;
  private Boolean isProfilePicture;
  /**
   * Constructs a new Media object.
   *
   * @param id the ID of the Media
   * @param owner the ID of the User who uploaded this Media
   * @param title the title of this Media
   * @param creation the creation time of this Media
   * @param content the content of this media
   */
  public Media(UUID id, UUID owner, String title, Instant creation, BufferedImage content, String contentType) {
    this.id = id;
    this.owner = owner;
    this.title = title;
    this.creation = creation;
    this.content = content;
    this.contentType = contentType;
    this.isProfilePicture = false;
  }

  /** Returns the ID of this Media. */
  public UUID getId() {
    return id;
  }

  /** Returns the ID of the User who created this Media. */
  public UUID getOwnerId() {
    return owner;
  }

  /** Returns the title of this Media. */
  public String getTitle() {
    return title;
  }

  /** Returns the creation time of this Media. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns the content of this Media. */
  public BufferedImage getContent() {
    return content;
  }

  public String getContentType() {
    return contentType;
  }

  public void setIsProfilePicture(Boolean bool) {
    isProfilePicture = bool;
  }

  public Boolean getIsProfilePicture() {
    return isProfilePicture;
  }
}