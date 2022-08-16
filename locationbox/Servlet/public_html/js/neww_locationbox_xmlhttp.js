  IXMLHttpRequest.count = 0;

  function IXMLHttpRequest() {
    this.url = null;
    this.element = null;
  }

  IXMLHttpRequest.prototype.setRequestHeader=function(hdr, val) {
  }

  IXMLHttpRequest.prototype.open=function(method, urlString, async) {
    this.url = urlString;
  }

  IXMLHttpRequest.prototype.send=function(data) {
    if( !this.url ) return;

    this.callBackId = Math.round(Math.random()*10000) + "_" + IXMLHttpRequest.count++;
    var urlString = this.url;
    if( urlString.indexOf("?") > 0 ) urlString += "&"; else urlString += "?";
    urlString += "callBackId=" + this.callBackId;

    var element = document.createElement("script");
    element.src = urlString;
    element.type = 'text/javascript';
    element.charset = 'utf-8';
    this.element = element;
    document.body.appendChild(element);
  }
