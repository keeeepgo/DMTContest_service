/**
* FancyNav - Mobile Navigation with CSS3 Transitions
* http://kratzik.com/fancynav
* Copyright (c) 2017 Johann Kratzik
* Version 1.0.0
*/
!
function(a, b) {
    "use strict";
    function c(b, c) {
        this.options = c,
        this.$currentLink = b,
        this.$rootBody = a("body"),
        void 0 !== b.data("fancynav-add") && (this.options = a.extend({},
        this.options, {
            add: b.data("fancynav-add")
        })),
        void 0 !== b.data("fancynav-animation") && (this.options = a.extend({},
        this.options, {
            animation: b.data("fancynav-animation")
        })),
        void 0 !== b.data("fancynav-class") && (this.options = a.extend({},
        this.options, {
            navClass: b.data("fancynav-class")
        })),
        void 0 !== b.data("fancynav-header") && (this.options = a.extend({},
        this.options, {
            navHeader: b.data("fancynav-header")
        })),
        void 0 !== b.data("fancynav-back") && (this.options = a.extend({},
        this.options, {
            backText: b.data("fancynav-back")
        })),
        this.init()
    }
    c.prototype = {
        init: function() {
            this.initNavLinkEvents()
        },
        initNavLinkEvents: function() {
            var a = this;
            this.$currentLink.on("click",
            function(b) {
                b.preventDefault(),
                a.initPageLayout(),
                a.generateNavBlock(),
                setTimeout(function() {
                    a.$rootBody.addClass("fancynav-opened")
                },
                10)
            })
        },
        initPageLayout: function() {
            this.$outerWrap = a('<div class="fancynav-outer"></div>'),
            this.$innerWrap = a('<div class="fancynav-inner"></div>'),
            this.$innerWrap.append(this.$rootBody.contents()),
            this.$outerWrap.append(this.$innerWrap),
            this.$rootBody.append(this.$outerWrap),
            this.initOverlay()
        },
        initOverlay: function() {
            var b = this;
            this.pageOverlay = a('<div class="fancynav-overlay">'),
            this.pageOverlay.on("click",
            function(a) {
                b.destroy()
            }),
            this.$innerWrap.append(this.pageOverlay)
        },
        generateNavBlock: function() {
            var b = this;
            this.$mainNavContent = a('<nav class="fancynav-mainnav"></nav>'),
            this.$mainNav = a("<ul>"),
            b.$rootBody.addClass("fancynav fancynav-animation-" + b.options.animation),
            b.options.navClass && b.$rootBody.addClass(b.options.navClass),
            this.$navHeader = a('<header class="fancynav-header"></header>'),
            this.$navTitle = a('<div class="fancynav-title"></div>'),
            this.$navTitle.text(b.options.navHeader),
            this.$navCloser = a('<span class="fancynav-close"></span>'),
            this.$navCloser.on("click",
            function(a) {
                b.destroy()
            }),
            this.navBlocksClone = this.convertIntoArray(this.options.add),
            this.navBlocksClone.forEach(function(c) {
                var d = a(c).clone();
                b.setNavLinksFromClone(d.contents())
            }),
            this.customizeNavBlock(),
            this.$mainNavContent.prepend(this.$navHeader).append(this.$mainNav),
            this.$outerWrap.prepend(this.$mainNavContent),
            this.$navHeader.prepend(this.$navTitle).append(this.$navCloser),
            this.listItems = a("li", this.$mainNavContent),
            this.listItems.each(function(b) {
                var c = b + 1;
                a(this).addClass("fancynav-item-" + c)
            })
        },
        convertIntoArray: function(b) {
            if ("string" == typeof b) {
                var c = a.map(b.split(","), a.trim);
                return c
            }
            return b
        },
        setNavLinksFromClone: function(a) {
            this.$mainNav.append(a)
        },
        customizeNavBlock: function() {
            var b = this,
            c = this.$mainNav.find('li:has("ul")');
            c.each(function() {
                var c = a(this),
                d = c.find("> a"),
                e = c.find("ul").eq(0);
                c.addClass("fancynav-has-inner");
                var f = b.options.backText ? b.options.backText: d.text(),
                g = a("<div>");
                g.addClass("fancynav-subnav");
                var h = a('<span class="fancynav-back"></span>');
                h.text(f),
                h.on("click",
                function(a) {
                    a.preventDefault(),
                    g.removeClass("fancynav-subnav-active")
                });
                var i = a('<span class="fancynav-next"></span>');
                i.on("click",
                function(a) {
                    a.preventDefault(),
                    g.hasClass("fancynav-subnav-active") || g.addClass("fancynav-subnav-active")
                }),
                d.append(i),
                g.append(h).append(e),
                c.append(g)
            })
        },
        destroy: function() {
            var a = this;
            a.$rootBody.find(".fancynav-mainnav");
            a.$rootBody.removeClass("fancynav-opened"),
            setTimeout(function() {
                a.$navCloser.off(),
                a.$currentLink.removeData("FancyNav"),
                a.pageOverlay.off(),
                a.pageOverlay.remove(),
                a.$mainNavContent.remove(),
                a.$rootBody.removeClass("fancynav fancynav-animation-" + a.options.animation).removeClass(a.options.navClass).append(a.$innerWrap.contents()),
                a.$outerWrap.remove()
            },
            a.options.hideDelay)
        }
    },
    a.fn.fancynav = function(b) {
        return b = a.extend({},
        {
            add: ".fancynav-add",
            animation: "slide-top",
            navClass: "",
            navHeader: "",
            backText: "",
            hideDelay: 510
        },
        b),
        this.each(function() {
            var d = a(this);
            d.data("FancyNav", new c(d, b))
        })
    }
} (jQuery, jQuery(window));