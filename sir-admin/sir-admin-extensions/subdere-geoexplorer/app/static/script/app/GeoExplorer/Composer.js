/**
 * Copyright (c) 2009-2010 The Open Planning Project
 *
 * @requires GeoExplorer.js
 */

/** api: (define)
 *  module = GeoExplorer
 *  class = GeoExplorer.Composer(config)
 *  extends = GeoExplorer
 */

/** api: constructor
 *  .. class:: GeoExplorer.Composer(config)
 *
 *      Create a GeoExplorer application intended for full-screen display.
 */
GeoExplorer.Composer = Ext.extend(PersistenceGeo.Composer, {

    // defaultRestUrl: '../../rest',
    // loginUrl: '../../j_spring_security_check',
    // logoutUrl: '../../logout',
    // adminUrl: '../../controlUsuarioLogado',
    defaultRestUrl: "/sir-subdere/rest",
    loginUrl: "/sir-subdere/j_spring_security_check",
    logoutUrl: "/sir-subdere/logout",
    adminUrl: '/sir-subdere/controlUsuarioLogado',
    trackingUrl: "/sir-subdere/stats",

    /* print config */
    pdfFooterText: "",
    logoDataUri: '',

    /** To be applied in Ext.Window **/
    constrainHeader: true,

    /** To be applied in Ext.Window **/
    minimizable: true,

    /** Fix to avoid #86234 as Arica Baselayer has no predefined bounding box **/
    initialCenter: [-7791752, -2074194],
    initialZoom: 8,

    /**
     * Base layers names: May be the same than defined in composer.html!!
     **/
    baseLayers: ["OpenStreetMap", "Google Satellite","Google Terrain"],

    constructor: function(config) {

        config.tools = [{
            ptype: 'gxp_localcertificates',
            actionTarget: 'map.tbar',
            before:{
                ptype: 'vw_wmsgetfeatureinfo'
            }
        },{
            actions: ['->']
        },{
            ptype: 'gxp_planificationtools',
            showButtonText: true
        }, {
            ptype: 'pgeo_chileindicainvestmentchart',
            showButtonText: true
        }, {

            actions: ['-']
        }];



        GeoExplorer.Composer.superclass.constructor.apply(this, arguments);

        this.on('ready', this._onLoadReady, this);

    },

    _onLoadReady: function() {
        Ext.Msg.show({
                buttons: Ext.MessageBox.OK,
                cls: 'legal-alert',
                icon: Ext.MessageBox.WARNING,
                minWidth: 300,
                title: 'Aviso Legal',
                msg: 'Todos los límites territoriales están sujetos a verificación.'
            });
    },

    loadConfig: function(config) {
        GeoExplorer.Composer.superclass.loadConfig.apply(this, arguments);

        // We override OpenLayer's formatting function here so we don't need
        // to fork OpenLayers and stuff.
        OpenLayers.Util.getFormattedLonLat = this._formatLatLongFunction;
    },

    getAditionalControls: function(){
        return [
            new OpenLayers.Control.CustomOverviewMap({
                viewFixed: true,
                fixedZoomLevel: 7,
                customCenter: this.initialCenter,
                layers: [new OpenLayers.Layer.WMS(
                        'OpenLayers WMS',
                        'http://vmap0.tiles.osgeo.org/wms/vmap0', {
                        layers: 'basic'
                    }), new OpenLayers.Layer.WMS(
                        'Comuna 2002',
                        this.sources.local.url.replace('/ows', '/wms'), {
                        layers: 'gore:comunas',
                        outputFormat: 'image/png',
                        transparent: true,
                        styles: 'Borde_comuna',
                        tiled: true,
                        version: '1.1.0'
                    })]
            })
        ];
    },

    /**
     * api: method[getNorthPanel]
     * Obtain header panel of the app.
     */
//    getNorthPanel: function(){
//        return  {
//            region: 'north',
//            id: 'viewer-header',
//            cls: 'viewer-header',
//            //collapsible: true,
//            height: 100,
//            layout: 'fit',
//            html: '<div id="responsive-wrap">' + '<div id="logo" class="span-4">' + '<a href="' + this.adminUrl + '" title="Inicio" class="logo-chile">' + '<img src="../theme/app/img/logo-gobierno_de_chile.png" alt="Logo Gobierno de Chile" height="100" />' + '</a>' + '</div>' + '<img src="../theme/app/img/back-header.jpg" alt="Imagen de fondo de la cabecera" class="banner" />' + '</div><!-- /#responsive-wrap -->'
//        };
//    },

    /**
     * api: method[getFooterPanel]
     * Obtain footer panel of the app.
     */
//    getFooterPanel: function(){
//        return {
//            region: 'south',
//            id: 'viewer-footer',
//            cls: 'viewer-footer',
//            height: 60,
//            layout: 'fit',
//            html: '<div id="footer">' + '<div class="span-24">' + '<a href="../../contacto/nuevoContacto" target="_blank" style="text-decoration: none;cursor:pointer;">Contacto</a>' + ' | <a href="../../faq/faqs/cartogr%C3%A1fico" target="_blank" style="text-decoration: none;cursor:pointer;">Preguntas frecuentes</a>' + '</div>' + '<div class="span-24" style="padding-bottom: 30px">' + '<p><strong>Gobierno Regional Región de Arica y Parinacota</strong></p>' + '</div>' + '</div>'
//        };
//    },
    
    /**
     * api: method[getFooterPanel]
     * Obtain footer panel of the app.
     */
    getFooterPanel: function(){
        return {
            region: 'south',
            id: 'viewer-footer',
            cls: 'viewer-footer',
            height: 60,
            layout: 'fit',
            html: '<div id="footer">' + '<div class="span-24">' + '<a href="../../contacto/nuevoContacto" target="_blank" style="text-decoration: none;cursor:pointer;">Contacto</a>' + ' | <a href="../../faq/faqs/cartogr%C3%A1fico" target="_blank" style="text-decoration: none;cursor:pointer;">Preguntas frecuentes</a>' + '</div>' + '<div class="span-24" style="padding-bottom: 30px">' + '</div>' + '</div>'
        };
    }

});
