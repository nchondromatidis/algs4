define(["jquery","underscore","js/lib/moment","js/lib/cookie","js/core/coursera"],function($,_,moment,cookie,Coursera){function handleAPIError(err,requestUrl){if(404==err)Coursera.router.trigger("error",404,requestUrl,{message:"this page does not exist"});else if(500==err)Coursera.router.trigger("error",500,requestUrl,{message:"backend server has seen an error"});else if(401==err)location.href=Coursera.config.url.accounts+"signin?post_redirect="+page;else if(403==err)Coursera.router.trigger("error",403,requestUrl,{message:"you do not have permission to view this page"})}function handleRedirect(redirect){if(redirect){var parse=/^(?:https?:\/\/)?((?:[\d\w\-]+?\.)?[\w\d]+(?:\.[\w]+)?)(\/.*|$)/,parts=parse.exec(redirect),host=(parts&&parts.length?parts[1]:"").split("."),hostm=parse.exec(location.host)[1].split(".");if(host&&host.length&&host[host.length-1]==hostm[hostm.length-1]&&host[host.length-2]==hostm[hostm.length-2])location.href=redirect;else if(/^\/.*/.test(redirect))location.href=redirect;else Coursera.router.navigate(Coursera.config.dir.home,!0)}}function redirectToAccounts(page){location.href=Coursera.config.url.accounts+page}function createAccountsUrl(page,params){if(void 0!==params){if(void 0!==params.r)params.post_redirect=params.r,delete params.r;var urlParams=$.param(params);return Coursera.config.url.accounts+page+"?"+urlParams.split("+").join("%20")}return Coursera.config.url.accounts+page}function scrollToInternalLink($dom,section){var $target=$dom.find('[data-section="'+section+'"]');if($target.length){var newTop=$target.position().top,scrollDiff=Math.abs($(document).scrollTop()-newTop);if(scrollDiff>1e3)$("html,body").scrollTop(newTop);else $("html,body").animate({scrollTop:newTop});if(Coursera.multitracker)Coursera.multitracker.push([document.title.split(" |")[0]+" "+section,"Open"])}}function setupInternalLinks(view){view.bind("view:appended",function(){scrollToInternalLink(view.$el,this.options.section)}),view.$el.on("click","a.internal-page",function(e){if(!e.ctrlKey&&!e.metaKey){var linkPath=e.currentTarget.pathname,section=linkPath.substr(linkPath.lastIndexOf("/")+1);if(view.$("[data-section="+section+"]").length)e.preventDefault(),scrollToInternalLink(view.$el,section),Coursera.router.navigateTo(linkPath,{trigger:!1})}})}function getToday(){return new Date}function prettyJoin(items){if(items.length>1)return items.slice(0,-1).join(", ")+" and "+items[items.length-1];else return items.toString()}function addCommas(numberStr){return numberStr.replace(/(^|[^\w.])(\d{4,})/g,function($0,$1,$2){return $1+$2.replace(/\d(?=(?:\d\d\d)+(?!\d))/g,"$&,")})}function makeHttps(url){return url.replace(/^http:\/\//i,"https://")}function getUrlParam(url,name){name=name.replace(/[\[]/,"\\[").replace(/[\]]/,"\\]");var regexS="[\\?&]"+name+"=([^&#]*)",regex=new RegExp(regexS),results=regex.exec(unescape(url));if(null===results)return null;else return results[1]}function changeUrlParam(uri,key,value){var re=new RegExp("([?&])"+key+"=[^&]*(&|$)","i");if(separator=-1!==uri.indexOf("?")?"&":"?",uri.match(re))return uri.replace(re,"$1"+key+"="+value+"$2");else return uri+separator+key+"="+value}function getUrlPath(url){return(url||"").replace(Coursera.config.url.base,"/")}function setupFormSave($form,xhrOptions,autoSave){function changeButtonText(message){var messageText=$saveButton.attr("data-"+message+"-message");$saveButton.html(messageText)}function resetButton(){var $requiredCheckboxes=$form.find('input[type="checkbox"]').filter("[required]");if($requiredCheckboxes.length&&!$requiredCheckboxes.is(":checked"))return void $saveButton.attr("disabled","disabled");else return $saveButton.removeAttr("disabled"),void changeButtonText("default")}function sendXHR(){lastFormData=$form.serialize();var type=(xhrOptions.type||"POST").toLowerCase();if(Coursera.api[type])Coursera.api[type](xhrOptions.url,{data:xhrOptions.data||$form.serialize()||null,headers:xhrOptions.headers||{},dataType:xhrOptions.dataType||"json"}).done(function(responseJSON,textStatus,xhr){if(xhrOptions.success)xhrOptions.success(responseJSON,textStatus,xhr);changeButtonText("success"),$saveStatus.html("(Saved!)"),renderAllErrors($form,null)}).fail(function(xhr,textStatus,errorThrown){var data=$.parseJSON(xhr.responseText);if(xhrOptions.error)xhrOptions.error(xhr,textStatus,errorThrown);resetButton(),$form.find("input").off(".validate"),renderAllErrors($form,data)})}function triggerSave(){if($form.serialize()!=lastFormData)$saveButton.trigger("click"),$saveStatus.html("(Saving...)")}xhrOptions=xhrOptions||{};var lastFormData=$form.serialize();xhrOptions.url=xhrOptions.url||$form.attr("action"),xhrOptions.type=xhrOptions.type||$form.attr("type");var $saveButton=$form.find('button[type="submit"]'),$saveStatus=$($saveButton.attr("data-update"));$form.find("input, select").on("change",resetButton),$form.find("input, textarea").on("keyup",resetButton);var $allInputs=$form.find("input, select");$allInputs.each(function(ind,elem){if(ind!=$allInputs.length-1)$(elem).on("keypress",function(event){return 13!=event.keyCode})});var shouldValidateForm="true"==$form.attr("data-validate");if(shouldValidateForm)$form.find("input").each(function(){var $field=$(this);if("checkbox"!=$field.attr("type"))$field.on("keyup.validate",function(){if(!validateField($field))renderFieldErrors($field,null)}),$field.on("blur.validate paste.validate",function(){renderFieldErrors($field,validateField($field))})});if($saveButton.on("click",function(event){if(event.preventDefault(),!shouldValidateForm||shouldValidateForm&&!validateForm($form))changeButtonText("inflight"),$saveButton.attr("disabled","disabled"),sendXHR(xhrOptions)}),autoSave){var triggerSaveD=_.debounce(triggerSave,1e3);$form.find("input, select").on("change.autosave",triggerSaveD),$form.find("input, textarea").on("keyup.autosave",triggerSaveD),$form.find("input, select, blur").on("blur.autosave paste.autosave",triggerSave)}}function validateForm($form){var foundError=null;if($form.find("input").each(function(){var $field=$(this),fieldError=validateField($field);if(renderFieldErrors($field,fieldError),foundError=foundError||fieldError,fieldError&&!foundError&&0===$("input:focus").length)$field.focus()}),foundError){var invalidMessage=$form.attr("data-invalid-message")||"Uh-oh, please check the form!";renderFormError($form,invalidMessage),$form.find('button[type="submit"]').animate({opacity:.4},100).animate({"margin-left":"-=10"},100).animate({"margin-left":"+=20"},100).animate({"margin-left":"-=20"},100).animate({"margin-left":"+=10"},100).animate({opacity:1},100)}return foundError}function validateField($field){var fieldType=$field.attr("type"),fieldValue=$field.val(),fieldPattern=$field.attr("pattern"),error=null,regEx=null;if("email"==fieldType)regEx=new RegExp(/[a-zA-Z0-9!#$%&'*+\/=?\^_`{|}~\-]+(?:\.[a-zA-Z0-9!#$%&'*+\/=?\^_`{|}~\-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9\-]*[a-zA-Z0-9])?\.)+[a-zA-Z0-9](?:[a-zA-Z0-9\-]*[a-zA-Z0-9])?/);else if(fieldPattern)regEx=new RegExp(fieldPattern);if(regEx)if(!regEx.test(fieldValue))error=$field.attr("data-invalid-message")||"Invalid field";if($field.attr("required")){if("text"==fieldType&&0===fieldValue.length)error="This field is required.";if("checkbox"==fieldType&&!$field.is(":checked"))error="⬆ Please check this!"}return error}function renderAllErrors($form,data){var $errorsDom;if($form.find(".form-errors").remove(),$form.find(".error .help-inline").remove(),$form.find(".error").removeClass("error"),data){if(data.error_message)renderFormError($form,data.error_message);if(data.field_errors)for(var fieldName in data.field_errors){var $fieldDom=$form.find('*[name="'+fieldName+'"]');renderFieldErrors($fieldDom,data.field_errors[fieldName]),$fieldDom.focus()}}}function renderFormError($form,error){if($form.find(".form-errors").remove(),$errorsDom=$("<span></span>").addClass("form-errors").attr("role","alert").attr("aria-live","assertive").html(error),$form.find(".actions").length)$form.find(".actions").children(":last").after($errorsDom);else if($form.find(".modal-footer").length)$form.find(".modal-footer").children(":last").after($errorsDom);else if($form.find('button[type="submit"]'))$form.find('button[type="submit"]').after($errorsDom)}function renderFieldErrors($fieldDom,fieldErrors){var $parentGroup=$fieldDom.parents(".control-mini-group").length&&$fieldDom.parents(".control-mini-group")||$fieldDom.parents(".control-group");if($parentGroup.removeClass("error"),$parentGroup.find(".help-inline.error").remove(),fieldErrors){var errorId=($fieldDom.attr("id")||"")+"-form-field-error-"+(new Date).getTime(),describedBy=($fieldDom.attr("aria-describedby")||"").split(",");if(describedBy.push(errorId),$fieldDom.attr("aria-describedby",describedBy.join(",")),$errorsDom=$("<span></span>").addClass("help-inline error").attr("role","alert").attr("aria-live","assertive").attr("id",errorId),fieldErrors instanceof Array)for(var i=0;i<fieldErrors.length;i++)$errorsDom.append("<span>"+fieldErrors[i]+"</span>");else $errorsDom.append(fieldErrors);if($parentGroup.addClass("error"),"checkbox"==$fieldDom.attr("type"))$parentGroup.find(".controls").append($errorsDom);else $fieldDom.after($errorsDom)}}function isMobileDevice(){var ua=navigator.userAgent||navigator.vendor||window.opera;return/iPhone|iPod|iPad|Android|BlackBerry|Opera Mini|IEMobile/.test(ua)}function isIOS(){var ua=navigator.userAgent||navigator.vendor||window.opera;return/iPhone|iPod|iPad/.test(ua)}function isTouchSupported(){return"ontouchstart"in window||window.DocumentTouch&&document instanceof DocumentTouch}function isCanvasSupported(){var elem=document.createElement("canvas");return!(!elem.getContext||!elem.getContext("2d"))}function getViewportHeight(){var height=window.innerHeight,mode=document.compatMode;if(mode||!$.support.boxModel)height="CSS1Compat"==mode?document.documentElement.clientHeight:document.body.clientHeight;return height}function inView($elem,nearThreshold){nearThreshold=nearThreshold||0;var viewportHeight=getViewportHeight(),scrollTop=document.documentElement.scrollTop?document.documentElement.scrollTop:document.body.scrollTop,startRange=scrollTop-nearThreshold,endRange=scrollTop+viewportHeight+nearThreshold,elemTop=$elem.offset().top,elemHeight=$elem.height(),elemBottom=elemTop+elemHeight;return endRange>elemBottom&&elemBottom>startRange}function countCS(courses){var numCS=0,csCats=["cs-programming","cs-systems","cs-theory","cs-ai","cs","fmgb"];return courses.each(function(course){if(course.get("topic")&&_.intersection(course.get("topic").get("category-ids"),csCats).length>0)numCS++}),numCS}function setupPromos(){var $promos=$("[data-promo-id]");$promos.each(function(){var $promo=$(this);if($promo.find(".close").length){var promoId=$promo.attr("data-promo-id");if(cookie.get(promoId))$promo.hide();else $promo.show(),$promo.find(".close").on("click",function(){$promo.hide(),cookie.set(promoId,"closed")})}})}function prettifyTime($dom){var dateTime=moment.unix($dom.attr("datetime"));if(Math.abs(moment().diff(dateTime))<6e4)$dom.text("just now");else $dom.text(dateTime.fromNow());$dom.attr("title",dateTime.format("dddd, MMMM Do YYYY, h:mm a Z"))}function prettifyUrl(url){return url=url.replace(/^.*:\/\//,""),url=url.replace(/\/$/,"")}function extractFirstSubdir(url){var urlRegex=/(.*\/).*\//;return urlRegex.exec(url)[1]}function linkifyText(string){if(string)string=string.replace(/((https?\:\/\/)|(www\.))(\S+)(\w{2,4})(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?/gi,function(url){var full_url=url;if(!full_url.match("^https?://"))full_url="http://"+full_url;return'<a target="_blank" href="'+full_url+'">'+url+"</a>"});return string}function rgbToHsl(r,g,b){r/=255,g/=255,b/=255;var max=Math.max(r,g,b),min=Math.min(r,g,b),h,s,l=(max+min)/2;if(max==min)h=s=0;else{var d=max-min;switch(s=l>.5?d/(2-max-min):d/(max+min),max){case r:h=(g-b)/d+(b>g?6:0);break;case g:h=(b-r)/d+2;break;case b:h=(r-g)/d+4}h/=6}return[h,s,l]}function hslToRgb(h,s,l){var r,g,b;if(0===s)r=g=b=l;else{var hue2rgb=function(p,q,t){if(0>t)t+=1;if(t>1)t-=1;if(1/6>t)return p+6*(q-p)*t;if(.5>t)return q;if(2/3>t)return p+(q-p)*(2/3-t)*6;else return p},q=.5>l?l*(1+s):l+s-l*s,p=2*l-q;r=hue2rgb(p,q,h+1/3),g=hue2rgb(p,q,h),b=hue2rgb(p,q,h-1/3)}return[Math.round(255*r),Math.round(255*g),Math.round(255*b)]}function rgbToHsv(r,g,b){r/=255,g/=255,b/=255;var max=Math.max(r,g,b),min=Math.min(r,g,b),h,s,v=max,d=max-min;if(s=0===max?0:d/max,max==min)h=0;else{switch(max){case r:h=(g-b)/d+(b>g?6:0);break;case g:h=(b-r)/d+2;break;case b:h=(r-g)/d+4}h/=6}return[h,s,v]}function hsvToRgb(h,s,v){var r,g,b,i=Math.floor(6*h),f=6*h-i,p=v*(1-s),q=v*(1-f*s),t=v*(1-(1-f)*s);switch(i%6){case 0:r=v,g=t,b=p;break;case 1:r=q,g=v,b=p;break;case 2:r=p,g=v,b=t;break;case 3:r=p,g=q,b=v;break;case 4:r=t,g=p,b=v;break;case 5:r=v,g=p,b=q}return[Math.round(255*r),Math.round(255*g),Math.round(255*b)]}function sortedGroupBy(list,groupByIter,sortByIter){var groups=_.groupBy(list,groupByIter);return groups=_.sortBy(groups,sortByIter)}function splitIntoGroups(list,nGroups){var groups=[];if(!list)return groups;for(var groupSize=Math.ceil(list.length/nGroups),i=0;i<list.length;i+=groupSize)groups.push(list.slice(i,i+groupSize));return groups}function verticalCenterEl($container,$el){$el.css("position","relative"),$el.css("top",$container.height()/2-$el.height()/2)}function concatName(nameObj){var full_name=nameObj.first_name;if(nameObj.middle_name)full_name=full_name+" "+nameObj.middle_name;if(nameObj.last_name)full_name=full_name+" "+nameObj.last_name;return full_name}function removeDiacritics(str){for(var i=0;i<defaultDiacriticsRemovalMap.length;i++)str=str.replace(defaultDiacriticsRemovalMap[i].letters,defaultDiacriticsRemovalMap[i].base);return str}function getCourseName(home_link){if(!_.isString(home_link))return null;var regexp=new RegExp("^https://([^.]+)\\.[^.]+\\.org/(.*)$"),match=regexp.exec(home_link);if(null===match)return null;else return{courseDomain:match[1],courseName:match[2]}}function isChina(){var subdomain=window.location.hostname.split(".")[0];return["cn","staging-china"].indexOf(subdomain)>=0}function styleRightToLeft($el){$el.find("[data-user-generated]").attr("dir","auto"),$el.find("[data-user-generated] code").attr("dir","ltr"),$el.find("[data-user-generated] > ol,ul").attr("dir","rtl"),$el.find("[data-user-generated] a,li").wrapInner("<bdi></bdi>")}var defaultDiacriticsRemovalMap=[{base:"A",letters:/[\u0041\u24B6\uFF21\u00C0\u00C1\u00C2\u1EA6\u1EA4\u1EAA\u1EA8\u00C3\u0100\u0102\u1EB0\u1EAE\u1EB4\u1EB2\u0226\u01E0\u00C4\u01DE\u1EA2\u00C5\u01FA\u01CD\u0200\u0202\u1EA0\u1EAC\u1EB6\u1E00\u0104\u023A\u2C6F]/g},{base:"AA",letters:/[\uA732]/g},{base:"AE",letters:/[\u00C6\u01FC\u01E2]/g},{base:"AO",letters:/[\uA734]/g},{base:"AU",letters:/[\uA736]/g},{base:"AV",letters:/[\uA738\uA73A]/g},{base:"AY",letters:/[\uA73C]/g},{base:"B",letters:/[\u0042\u24B7\uFF22\u1E02\u1E04\u1E06\u0243\u0182\u0181]/g},{base:"C",letters:/[\u0043\u24B8\uFF23\u0106\u0108\u010A\u010C\u00C7\u1E08\u0187\u023B\uA73E]/g},{base:"D",letters:/[\u0044\u24B9\uFF24\u1E0A\u010E\u1E0C\u1E10\u1E12\u1E0E\u0110\u018B\u018A\u0189\uA779]/g},{base:"DZ",letters:/[\u01F1\u01C4]/g},{base:"Dz",letters:/[\u01F2\u01C5]/g},{base:"E",letters:/[\u0045\u24BA\uFF25\u00C8\u00C9\u00CA\u1EC0\u1EBE\u1EC4\u1EC2\u1EBC\u0112\u1E14\u1E16\u0114\u0116\u00CB\u1EBA\u011A\u0204\u0206\u1EB8\u1EC6\u0228\u1E1C\u0118\u1E18\u1E1A\u0190\u018E]/g},{base:"F",letters:/[\u0046\u24BB\uFF26\u1E1E\u0191\uA77B]/g},{base:"G",letters:/[\u0047\u24BC\uFF27\u01F4\u011C\u1E20\u011E\u0120\u01E6\u0122\u01E4\u0193\uA7A0\uA77D\uA77E]/g},{base:"H",letters:/[\u0048\u24BD\uFF28\u0124\u1E22\u1E26\u021E\u1E24\u1E28\u1E2A\u0126\u2C67\u2C75\uA78D]/g},{base:"I",letters:/[\u0049\u24BE\uFF29\u00CC\u00CD\u00CE\u0128\u012A\u012C\u0130\u00CF\u1E2E\u1EC8\u01CF\u0208\u020A\u1ECA\u012E\u1E2C\u0197]/g},{base:"J",letters:/[\u004A\u24BF\uFF2A\u0134\u0248]/g},{base:"K",letters:/[\u004B\u24C0\uFF2B\u1E30\u01E8\u1E32\u0136\u1E34\u0198\u2C69\uA740\uA742\uA744\uA7A2]/g},{base:"L",letters:/[\u004C\u24C1\uFF2C\u013F\u0139\u013D\u1E36\u1E38\u013B\u1E3C\u1E3A\u0141\u023D\u2C62\u2C60\uA748\uA746\uA780]/g},{base:"LJ",letters:/[\u01C7]/g},{base:"Lj",letters:/[\u01C8]/g},{base:"M",letters:/[\u004D\u24C2\uFF2D\u1E3E\u1E40\u1E42\u2C6E\u019C]/g},{base:"N",letters:/[\u004E\u24C3\uFF2E\u01F8\u0143\u00D1\u1E44\u0147\u1E46\u0145\u1E4A\u1E48\u0220\u019D\uA790\uA7A4]/g},{base:"NJ",letters:/[\u01CA]/g},{base:"Nj",letters:/[\u01CB]/g},{base:"O",letters:/[\u004F\u24C4\uFF2F\u00D2\u00D3\u00D4\u1ED2\u1ED0\u1ED6\u1ED4\u00D5\u1E4C\u022C\u1E4E\u014C\u1E50\u1E52\u014E\u022E\u0230\u00D6\u022A\u1ECE\u0150\u01D1\u020C\u020E\u01A0\u1EDC\u1EDA\u1EE0\u1EDE\u1EE2\u1ECC\u1ED8\u01EA\u01EC\u00D8\u01FE\u0186\u019F\uA74A\uA74C]/g},{base:"OI",letters:/[\u01A2]/g},{base:"OO",letters:/[\uA74E]/g},{base:"OU",letters:/[\u0222]/g},{base:"P",letters:/[\u0050\u24C5\uFF30\u1E54\u1E56\u01A4\u2C63\uA750\uA752\uA754]/g},{base:"Q",letters:/[\u0051\u24C6\uFF31\uA756\uA758\u024A]/g},{base:"R",letters:/[\u0052\u24C7\uFF32\u0154\u1E58\u0158\u0210\u0212\u1E5A\u1E5C\u0156\u1E5E\u024C\u2C64\uA75A\uA7A6\uA782]/g},{base:"S",letters:/[\u0053\u24C8\uFF33\u1E9E\u015A\u1E64\u015C\u1E60\u0160\u1E66\u1E62\u1E68\u0218\u015E\u2C7E\uA7A8\uA784]/g},{base:"T",letters:/[\u0054\u24C9\uFF34\u1E6A\u0164\u1E6C\u021A\u0162\u1E70\u1E6E\u0166\u01AC\u01AE\u023E\uA786]/g},{base:"TZ",letters:/[\uA728]/g},{base:"U",letters:/[\u0055\u24CA\uFF35\u00D9\u00DA\u00DB\u0168\u1E78\u016A\u1E7A\u016C\u00DC\u01DB\u01D7\u01D5\u01D9\u1EE6\u016E\u0170\u01D3\u0214\u0216\u01AF\u1EEA\u1EE8\u1EEE\u1EEC\u1EF0\u1EE4\u1E72\u0172\u1E76\u1E74\u0244]/g},{base:"V",letters:/[\u0056\u24CB\uFF36\u1E7C\u1E7E\u01B2\uA75E\u0245]/g},{base:"VY",letters:/[\uA760]/g},{base:"W",letters:/[\u0057\u24CC\uFF37\u1E80\u1E82\u0174\u1E86\u1E84\u1E88\u2C72]/g},{base:"X",letters:/[\u0058\u24CD\uFF38\u1E8A\u1E8C]/g},{base:"Y",letters:/[\u0059\u24CE\uFF39\u1EF2\u00DD\u0176\u1EF8\u0232\u1E8E\u0178\u1EF6\u1EF4\u01B3\u024E\u1EFE]/g},{base:"Z",letters:/[\u005A\u24CF\uFF3A\u0179\u1E90\u017B\u017D\u1E92\u1E94\u01B5\u0224\u2C7F\u2C6B\uA762]/g},{base:"a",letters:/[\u0061\u24D0\uFF41\u1E9A\u00E0\u00E1\u00E2\u1EA7\u1EA5\u1EAB\u1EA9\u00E3\u0101\u0103\u1EB1\u1EAF\u1EB5\u1EB3\u0227\u01E1\u00E4\u01DF\u1EA3\u00E5\u01FB\u01CE\u0201\u0203\u1EA1\u1EAD\u1EB7\u1E01\u0105\u2C65\u0250]/g},{base:"aa",letters:/[\uA733]/g},{base:"ae",letters:/[\u00E6\u01FD\u01E3]/g},{base:"ao",letters:/[\uA735]/g},{base:"au",letters:/[\uA737]/g},{base:"av",letters:/[\uA739\uA73B]/g},{base:"ay",letters:/[\uA73D]/g},{base:"b",letters:/[\u0062\u24D1\uFF42\u1E03\u1E05\u1E07\u0180\u0183\u0253]/g},{base:"c",letters:/[\u0063\u24D2\uFF43\u0107\u0109\u010B\u010D\u00E7\u1E09\u0188\u023C\uA73F\u2184]/g},{base:"d",letters:/[\u0064\u24D3\uFF44\u1E0B\u010F\u1E0D\u1E11\u1E13\u1E0F\u0111\u018C\u0256\u0257\uA77A]/g},{base:"dz",letters:/[\u01F3\u01C6]/g},{base:"e",letters:/[\u0065\u24D4\uFF45\u00E8\u00E9\u00EA\u1EC1\u1EBF\u1EC5\u1EC3\u1EBD\u0113\u1E15\u1E17\u0115\u0117\u00EB\u1EBB\u011B\u0205\u0207\u1EB9\u1EC7\u0229\u1E1D\u0119\u1E19\u1E1B\u0247\u025B\u01DD]/g},{base:"f",letters:/[\u0066\u24D5\uFF46\u1E1F\u0192\uA77C]/g},{base:"g",letters:/[\u0067\u24D6\uFF47\u01F5\u011D\u1E21\u011F\u0121\u01E7\u0123\u01E5\u0260\uA7A1\u1D79\uA77F]/g},{base:"h",letters:/[\u0068\u24D7\uFF48\u0125\u1E23\u1E27\u021F\u1E25\u1E29\u1E2B\u1E96\u0127\u2C68\u2C76\u0265]/g},{base:"hv",letters:/[\u0195]/g},{base:"i",letters:/[\u0069\u24D8\uFF49\u00EC\u00ED\u00EE\u0129\u012B\u012D\u00EF\u1E2F\u1EC9\u01D0\u0209\u020B\u1ECB\u012F\u1E2D\u0268\u0131]/g},{base:"j",letters:/[\u006A\u24D9\uFF4A\u0135\u01F0\u0249]/g},{base:"k",letters:/[\u006B\u24DA\uFF4B\u1E31\u01E9\u1E33\u0137\u1E35\u0199\u2C6A\uA741\uA743\uA745\uA7A3]/g},{base:"l",letters:/[\u006C\u24DB\uFF4C\u0140\u013A\u013E\u1E37\u1E39\u013C\u1E3D\u1E3B\u017F\u0142\u019A\u026B\u2C61\uA749\uA781\uA747]/g},{base:"lj",letters:/[\u01C9]/g},{base:"m",letters:/[\u006D\u24DC\uFF4D\u1E3F\u1E41\u1E43\u0271\u026F]/g},{base:"n",letters:/[\u006E\u24DD\uFF4E\u01F9\u0144\u00F1\u1E45\u0148\u1E47\u0146\u1E4B\u1E49\u019E\u0272\u0149\uA791\uA7A5]/g},{base:"nj",letters:/[\u01CC]/g},{base:"o",letters:/[\u006F\u24DE\uFF4F\u00F2\u00F3\u00F4\u1ED3\u1ED1\u1ED7\u1ED5\u00F5\u1E4D\u022D\u1E4F\u014D\u1E51\u1E53\u014F\u022F\u0231\u00F6\u022B\u1ECF\u0151\u01D2\u020D\u020F\u01A1\u1EDD\u1EDB\u1EE1\u1EDF\u1EE3\u1ECD\u1ED9\u01EB\u01ED\u00F8\u01FF\u0254\uA74B\uA74D\u0275]/g},{base:"oi",letters:/[\u01A3]/g},{base:"ou",letters:/[\u0223]/g},{base:"oo",letters:/[\uA74F]/g},{base:"p",letters:/[\u0070\u24DF\uFF50\u1E55\u1E57\u01A5\u1D7D\uA751\uA753\uA755]/g},{base:"q",letters:/[\u0071\u24E0\uFF51\u024B\uA757\uA759]/g},{base:"r",letters:/[\u0072\u24E1\uFF52\u0155\u1E59\u0159\u0211\u0213\u1E5B\u1E5D\u0157\u1E5F\u024D\u027D\uA75B\uA7A7\uA783]/g},{base:"s",letters:/[\u0073\u24E2\uFF53\u00DF\u015B\u1E65\u015D\u1E61\u0161\u1E67\u1E63\u1E69\u0219\u015F\u023F\uA7A9\uA785\u1E9B]/g},{base:"t",letters:/[\u0074\u24E3\uFF54\u1E6B\u1E97\u0165\u1E6D\u021B\u0163\u1E71\u1E6F\u0167\u01AD\u0288\u2C66\uA787]/g},{base:"tz",letters:/[\uA729]/g},{base:"u",letters:/[\u0075\u24E4\uFF55\u00F9\u00FA\u00FB\u0169\u1E79\u016B\u1E7B\u016D\u00FC\u01DC\u01D8\u01D6\u01DA\u1EE7\u016F\u0171\u01D4\u0215\u0217\u01B0\u1EEB\u1EE9\u1EEF\u1EED\u1EF1\u1EE5\u1E73\u0173\u1E77\u1E75\u0289]/g},{base:"v",letters:/[\u0076\u24E5\uFF56\u1E7D\u1E7F\u028B\uA75F\u028C]/g},{base:"vy",letters:/[\uA761]/g},{base:"w",letters:/[\u0077\u24E6\uFF57\u1E81\u1E83\u0175\u1E87\u1E85\u1E98\u1E89\u2C73]/g},{base:"x",letters:/[\u0078\u24E7\uFF58\u1E8B\u1E8D]/g},{base:"y",letters:/[\u0079\u24E8\uFF59\u1EF3\u00FD\u0177\u1EF9\u0233\u1E8F\u00FF\u1EF7\u1E99\u1EF5\u01B4\u024F\u1EFF]/g},{base:"z",letters:/[\u007A\u24E9\uFF5A\u017A\u1E91\u017C\u017E\u1E93\u1E95\u01B6\u0225\u0240\u2C6C\uA763]/g}];return{handleAPIError:handleAPIError,handleRedirect:handleRedirect,redirectToAccounts:redirectToAccounts,createAccountsUrl:createAccountsUrl,setupInternalLinks:setupInternalLinks,setupFormSave:setupFormSave,renderAllErrors:renderAllErrors,renderFieldErrors:renderFieldErrors,isMobileDevice:isMobileDevice,isIOS:isIOS,isTouchSupported:isTouchSupported,isCanvasSupported:isCanvasSupported,scrollToInternalLink:scrollToInternalLink,makeHttps:makeHttps,getToday:getToday,prettyJoin:prettyJoin,getUrlParam:getUrlParam,changeUrlParam:changeUrlParam,getUrlPath:getUrlPath,inView:inView,countCS:countCS,setupPromos:setupPromos,prettifyTime:prettifyTime,prettifyUrl:prettifyUrl,addCommas:addCommas,extractFirstSubdir:extractFirstSubdir,linkifyText:linkifyText,rgbToHsl:rgbToHsl,hslToRgb:hslToRgb,rgbToHsv:rgbToHsv,hsvToRgb:hsvToRgb,verticalCenterEl:verticalCenterEl,sortedGroupBy:sortedGroupBy,splitIntoGroups:splitIntoGroups,concatName:concatName,removeDiacritics:removeDiacritics,getCourseName:getCourseName,isChina:isChina,styleRightToLeft:styleRightToLeft}});