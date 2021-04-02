// ==UserScript==
// @name         Webpage Hint Bridge
// @namespace    http://kkdev.org/tcd/bhb
// @version      0.1
// @description  try to take over the world!
// @author       You
// @match        http://kkdev.org/*
// @icon         data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==
// @grant        GM.xmlHttpRequest
// @grant        GM_xmlHttpRequest
// @grant        GM_log
// @require      https://code.jquery.com/jquery-3.6.0.min.js
// ==/UserScript==

(function() {
    'use strict';


    //https://www.monperrus.net/martin/greasemonkey+jquery+and+xmlhttprequest+together
    function GM_XHR() {
        this.type = null;
        this.url = null;
        this.async = null;
        this.username = null;
        this.password = null;
        this.status = null;
        this.headers = {};
        this.readyState = null;

        this.abort = function() {
            this.readyState = 0;
        };

        this.getAllResponseHeaders = function(name) {
            if (this.readyState!=4) return "";
            return this.responseHeaders;
        };

        this.getResponseHeader = function(name) {
            var regexp = new RegExp('^'+name+': (.*)$','im');
            var match = regexp.exec(this.responseHeaders);
            if (match) { return match[1]; }
            return '';
        };

        this.open = function(type, url, async, username, password) {
            this.type = type ? type : null;
            this.url = url ? url : null;
            this.async = async ? async : null;
            this.username = username ? username : null;
            this.password = password ? password : null;
            this.readyState = 1;
        };

        this.setRequestHeader = function(name, value) {
            this.headers[name] = value;
        };

        this.send = function(data) {
            this.data = data;
            var that = this;
            // http://wiki.greasespot.net/GM_xmlhttpRequest
            GM.xmlHttpRequest({
                method: this.type,
                url: this.url,
                headers: this.headers,
                data: this.data,
                onload: function(rsp) {
                    // Populate wrapper object with returned data
                    // including the Greasemonkey specific "responseHeaders"
                    for (var k in rsp) {
                        that[k] = rsp[k];
                    }
                    // now we call onreadystatechange
                    that.onreadystatechange();
                },
                onerror: function(rsp) {
                    for (var k in rsp) {
                        that[k] = rsp[k];
                    }
                }
            });
        };
    };


    // Your code here...
    function MarkupRegion(Domnode){
        let texts = new Map();

        $("p").each((i,e)=>{
            let content = e.innerHTML;
            //GM_log(content);
            if(content!=null){
                let words = content.match(/\b(\w+)\b/g);
                words.forEach((ew)=>{
                    ew = ew.toLowerCase();
                    let elementArray=new Array();
                    if(texts.has(ew)){
                        elementArray = texts.get(ew);
                    }
                    if(elementArray.length!==0){
                        let last = elementArray.pop();
                        if(last!=e){
                            elementArray.push(last);
                        }
                    }
                    elementArray.push(e);
                    texts.set(ew, elementArray);
                });
            }

        });

        GM_log(texts);

        function Apply(result){
            result.forEach((e)=>{
                let elements = texts.get(e);
                let synid = "hint_" + e;
                elements.forEach((domele)=>{
                    if(!domele.innerHTML.includes(synid)){
                        domele.innerHTML = domele.innerHTML.replace(e+" ", "<span class=\""+synid+"\">" + e + "</span> ");
                    }

                });
                //GM_log($("."+synid));
                //$("."+synid).click((e)=>{});
                $(document).on('mouseover', "." + synid ,function (v) {
                    GM_log(e);
                });

                $(document).on('mouseout', "." + synid, function (v) {
                    GM_log(e + "out");
                });
            });

        }

        let result = Array.from( texts.keys() );
        Apply(result);
        $.ajax({
            url: 'http://127.0.0.1:1122/check',// this even works for cross-domain requests by default
            xhr: function(){return new GM_XHR();},
            type: 'POST',
            data: {
                payload: Array.from( texts.keys() )
            },
            success: function(val){
                //Apply markup
                let result = Array.from( texts.keys() );
                Apply(result);
            },
            error: function(err){
                GM_log(err);
            }
        });

    };


    $.ajax({
        url: 'http://127.0.0.1:1122/test',// this even works for cross-domain requests by default
        xhr: function(){return new GM_XHR();},
        type: 'POST',
        success: function(val){

        },
        error: function(err){
            GM_log(err);
        }
    });

    GM_log("Test");
    MarkupRegion(document);
})();