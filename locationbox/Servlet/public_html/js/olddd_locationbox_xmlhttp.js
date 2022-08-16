  IXMLHttpRequest.array = new Array();
  IXMLHttpRequest.count = 0;

  function IXMLHttpRequest() {
    this.onreadystatechange = null;
    this.callBackId = 0;
    this.url = null;
    this.status = 0;
    this.readyState = 0;
    this.element = null;
    this.responseText = null;
  }

  IXMLHttpRequest.callBack=function(id, responseText, xx) {
    alert(id);
    while( IXMLHttpRequest.array.length > 0 ) {
      var httpReq = IXMLHttpRequest.array[0];
      if( !httpReq.onreadystatechange ) {
        if( httpReq.element ) {
          document.body.removeChild(httpReq.element);
          httpReq.element = null;
        }
        IXMLHttpRequest.array.shift();
      }
      else
        break;
    } // while()

    for( var i = 0; i < IXMLHttpRequest.array.length; i++ ) {
      if( IXMLHttpRequest.array[i].callBackId == id ) {
        var httpReq = IXMLHttpRequest.array[i];
        httpReq.status = 200;
        httpReq.readyState = 4;
        httpReq.responseText = responseText;
        if( httpReq.onreadystatechange && !xx ) httpReq.onreadystatechange();
        httpReq.onreadystatechange = null;
        document.body.removeChild(httpReq.element);
        httpReq.element = null;
        if( i == 0 ) IXMLHttpRequest.array.shift();
        return;
      }

    } // for()
  }

  IXMLHttpRequest.prototype.abort=function() {
    IXMLHttpRequest.callBack(this.callBackId, null, true);
  }

  IXMLHttpRequest.prototype.open=function(x4, x5, x6) {
    this.url = x5;
  }

  IXMLHttpRequest.prototype.cb=function() {
    alert(this.callBackId);
    if( this.element == null ) alert("NULL");

    alert("aa");
    while( !result ) ;
    alert("result: " + result);
  }

  IXMLHttpRequest.prototype.send=function(data) {
    if( !this.url ) return;

    this.callBackId = Math.round(Math.random()*10000) + "_" + IXMLHttpRequest.count++;
    var urlString = this.url;
    if( urlString.indexOf("?") > 0 ) urlString += "&"; else urlString += "?";
    urlString += "callBackId=" + this.callBackId;
    urlString += "&" + data;
    if( urlString.length > 2000 ) {
      alert("Request Too Long!");
      return;
    }

    var element = document.createElement("script");
//    element.onload = IXMLHttpRequest.callBack(this.callBackId);
    element.src = urlString;
    element.type = 'text/javascript';
    element.charset = 'utf-8';
    this.element = element;
//    element.onload = this.cb();
    IXMLHttpRequest.array.push(this);
    document.body.appendChild(element);
  }

  IXMLHttpRequest.prototype.setRequestHeader=function(hdr, val) {
  }

