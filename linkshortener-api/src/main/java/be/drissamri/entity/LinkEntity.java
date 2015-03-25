/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Driss Amri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package be.drissamri.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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

  public LinkEntity() {
  }

  public LinkEntity(final String url, final String hash) {
    this.hash = hash;
    this.url = url;
  }

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUrl() {
    return this.url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getHash() {
    return this.hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public ZonedDateTime getCreatedDate() {
    return this.createdDate;
  }

  public void setCreatedDate(ZonedDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public ZonedDateTime getModifiedDate() {
    return this.modifiedDate;
  }

  public void setModifiedDate(ZonedDateTime modifiedDate) {
    this.modifiedDate = modifiedDate;
  }

  public long getVersion() {
    return this.version;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id, this.url, this.hash);
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
      "id=" + Objects.toString(this.id) +
      ", url='" + Objects.toString(this.url) +
      ", hash='" + Objects.toString(this.hash) +
      ", createdDate=" + Objects.toString(this.createdDate) +
      ", modifiedDate=" + Objects.toString(this.modifiedDate) + '}';
  }
}
