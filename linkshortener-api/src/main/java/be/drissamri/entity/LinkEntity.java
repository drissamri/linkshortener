package be.drissamri.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "LINK")
public class LinkEntity implements Serializable {
  @JsonIgnore
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(name = "url", nullable = false, unique = true)
  private String url;
  @Column(name = "hash", nullable = false, unique = true)
  private String hash;

  @JsonIgnore
  @CreatedDate
  private ZonedDateTime createdDate;
  @JsonIgnore
  @LastModifiedDate
  private ZonedDateTime modifiedDate;
  @JsonIgnore
  @Version
  private long version;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public ZonedDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public ZonedDateTime getModifiedDate() {
    return modifiedDate;
  }

  public void setModifiedDate(ZonedDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public long getVersion() {
    return version;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, url, hash);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    final LinkEntity other = (LinkEntity) obj;
    return Objects.equals(this.id, other.id) && Objects.equals(this.url, other.url) && Objects.equals(this.hash, other.hash);
  }

  @Override
  public String toString() {
    return "LinkEntity { " +
      "id=" + Objects.toString(id) +
      ", url='" + Objects.toString(url) +
      ", hash='" + Objects.toString(hash) +
      ", createdDate=" + Objects.toString(createdDate) +
      ", modifiedDate=" + Objects.toString(modifiedDate) + '}';
  }
}