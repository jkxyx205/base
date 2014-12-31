/*!
 * FormValidation (http://formvalidation.io)
 * The best jQuery plugin to validate form fields. Support Bootstrap, Foundation, Pure, SemanticUI, UIKit frameworks
 *
 * @version     v0.6.0-dev, built on 2014-12-29 10:33:07 AM
 * @author      https://twitter.com/nghuuphuoc
 * @copyright   (c) 2013 - 2015 Nguyen Huu Phuoc
 * @license     http://formvalidation.io/license/
 */
!function(a){FormValidation.Framework.UIKit=function(b,c){c=a.extend(!0,{button:{selector:'[type="submit"]',disabled:"disabled"},control:{valid:"uk-form-success",invalid:"uk-form-danger"},err:{clazz:"uk-text-warning",parent:"^.*(uk-form-controls|uk-width-[\\d+]-[\\d+]).*$"},icon:{valid:null,invalid:null,validating:null,feedback:"fv-control-feedback"},row:{selector:".uk-form-row",valid:"",invalid:"",feedback:"fv-has-feedback"}},c),FormValidation.Base.apply(this,[b,c])},FormValidation.Framework.UIKit.prototype=a.extend({},FormValidation.Base.prototype,{_fixIcon:function(a,b){var c=a.attr("type");if("checkbox"===c||"radio"===c){var d=a.parent();d.is("label")&&b.insertAfter(d)}},_createTooltip:function(b,c){var d=b.data("fv.icon");d&&(d.data("tooltip")&&(d.data("tooltip").off(),d.removeData("tooltip")),d.attr("title",c).css({cursor:"pointer"}),new a.UIkit.tooltip(d))},_destroyTooltip:function(a){var b=a.data("fv.icon");if(b){var c=b.data("tooltip");c&&(c.hide(),c.off(),b.off("focus mouseenter").removeData("tooltip")),b.css({cursor:""})}},_hideTooltip:function(a){var b=a.data("fv.icon");if(b){var c=b.data("tooltip");c&&c.hide(),b.css({cursor:""})}},_showTooltip:function(a){var b=a.data("fv.icon");if(b){b.css({cursor:"pointer"});var c=b.data("tooltip");c&&c.show()}}})}(jQuery);