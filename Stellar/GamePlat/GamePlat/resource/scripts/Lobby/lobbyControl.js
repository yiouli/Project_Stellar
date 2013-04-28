(function(){
    var defaults = {};
    window.Stellar = window.Stellar || {};
    var namespace = window.Stellar;

    var lobbyModel = new namespace.LobbyModel(defaults);
    var lobbyView = new namespace.LobbyView({model: lobbyModel, el: $("#lobby_containter")});

}());