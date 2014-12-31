/*!
 * FormValidation (http://formvalidation.io)
 * The best jQuery plugin to validate form fields. Support Bootstrap, Foundation, Pure, SemanticUI, UIKit frameworks
 *
 * @version     v0.6.0-dev, built on 2014-12-29 10:33:07 AM
 * @author      https://twitter.com/nghuuphuoc
 * @copyright   (c) 2013 - 2015 Nguyen Huu Phuoc
 * @license     http://formvalidation.io/license/
 */
!function(a){FormValidation.Framework.Pure=function(b,c){c=a.extend(!0,{button:{selector:'[type="submit"]',disabled:"pure-button-disabled"},err:{clazz:"fv-help-block",parent:"^.*pure-control-group.*$"},icon:{valid:null,invalid:null,validating:null,feedback:"fv-control-feedback"},row:{selector:".pure-control-group",valid:"",invalid:"",feedback:"fv-has-feedback"}},c),FormValidation.Base.apply(this,[b,c])},FormValidation.Framework.Pure.prototype=a.extend({},FormValidation.Base.prototype,{})}(jQuery);