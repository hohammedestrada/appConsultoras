const fs = require("fs");

function fnGetPais(data, Pais) {
    for (var x in data.locator.selectPais) {
        if (x == Pais) {
            return data.locator.selectPais[x];
        }
    }
}

function fnGetPaisSoporte(data, Pais) {
    for (var x in data.locator.selectPaisSoporte) {
        if (x == Pais) {
            return data.locator.selectPaisSoporte[x];
        }
    }
}

function fnGetPaisLogin(data, Pais) {
    for (var x in data.locator.selectPaisLogin) {
        if (x == Pais) {
            return data.locator.selectPaisLogin[x];
        }
    }
}

function fnGetFiles(dir, filter = "", files_) {
    files_ = files_ || [];
    var files = fs.readdirSync(dir);
    for (var i in files) {
        var name = dir + "/" + files[i];
        if (fs.statSync(name).isDirectory()) {
            fnGetFiles(name, filter, files_);
        } else if (name.indexOf(filter) >= 0) {
            files_.push(name);
        }
    }
    return files_;
}

module.exports.fnGetPais = fnGetPais;
module.exports.fnGetPaisSoporte = fnGetPaisSoporte;
module.exports.fnGetPaisLogin = fnGetPaisLogin;
module.exports.fnGetFiles = fnGetFiles;
