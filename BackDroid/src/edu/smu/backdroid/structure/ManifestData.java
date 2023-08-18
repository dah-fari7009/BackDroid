package edu.smu.backdroid.structure;

/*
 * Copyright (C) 2015 The Pennsylvania State University and the University of Wisconsin
 * Systems and Internet Infrastructure Security Laboratory
 *
 * Author: Damien Octeau
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


public class ManifestData {
  private String scheme;
  private String host;
  private String port;
  private String path;
  private String mimeType;

  public ManifestData() {
  }

  public ManifestData(String scheme, String host, String port, String path, String mimeType) {
    this.scheme = scheme;
    this.host = host;
    this.port = port;
    this.path = path;
    this.mimeType = mimeType;
  }

  //<scheme>://<host>:<port>/<path>
  /**
   * If a scheme is not specified, the host is ignored.
   * If a host is not specified, the port is ignored.
   * If both the scheme and host are not specified, the path is ignored.
   * @return
   */
  public String asUri(){
    StringBuilder result = new StringBuilder("");

    if(scheme != null && !scheme.isBlank()){
        result.append(scheme);
        if(host != null && !host.isBlank()){
            result.append("://"); //should this be added here or to scheme
            result.append(host);
            if(port != null && !port.isBlank()){
                result.append(":");
                result.append(port);
            }
        }
        if(path != null && !path.isBlank() && !path.equals("/")){
            result.append("/");
            result.append(path);
        }
    }
    return result.toString()
  }

  /**
   * @param scheme the scheme to set
   */
  public void setScheme(String scheme) {
    this.scheme = scheme;
  }

  /**
   * @param host the host to set
   */
  public void setHost(String host) {
    this.host = host;
  }

  /**
   * @param port the port to set
   */
  public void setPort(String port) {
    this.port = port;
  }

  /**
   * @param path the path to set
   */
  public void setPath(String path) {
    this.path = path;
  }

  /**
   * @param mimeType the mimeType to set
   */
  public void setMimeType(String mimeType) {
    this.mimeType = mimeType;
  }

  /**
   * @return the scheme
   */
  public String getScheme() {
    return scheme;
  }

  /**
   * @return the host
   */
  public String getHost() {
    return host;
  }

  /**
   * @return the port
   */
  public String getPort() {
    return port;
  }

  /**
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * @return the mimeType
   */
  public String getMimeType() {
    return mimeType;
  }

  public String toString(String indent) {
    StringBuilder result = new StringBuilder();
    result.append(indent + "  scheme=" + scheme + ", host=" + host + ", port" + port + ", path"
        + path + ", MIME type=" + mimeType);
    return result.toString();
  }

  @Override
  public String toString() {
    return toString("");
  }
}