/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//define common variables
var EXISTED_USER = 1000;
var STATUS_CODE_OK = 200;
var CREATE = 1;
var EDIT = 2;
var DELETE = 3;
var DELETE_USER_GROUP = 4;
var SEND_MESSAGE = 5;
var EXISTED_MESSAGE = 1000;
var STATUS_CODE_NOT_FOUND = 404;
var LOGIN = 6;
var LOGOUT = 7;
var GET_LIST = 8;
var GET_ROLE_BY_USER_AND_GROUP = 9;
var GET_USER_BYGROUP = 10;
var VIEW = 11;
var GET_ALL_LIST = 12;
var DELETE_USER_BY_EMAIL = 13;
var GET_ALL_DISTINCT_LIST = 14;
var GET_LIST_BY_GROUP_AND_KEY = 15;
var REMOVE_USER_FROM_GROUP = 16;
var GET_ROLE_OF_GROUP = 17;
var GET_REMAINING_USERS = 18;
var GET_ROLE_USER_OF_GROUP = 19;
var DELETE_TEMPLATE_BY_ID = 20;
var DOWNLOAD_LOG_CSV = 21;
var ADD_USER_TO_GROUP = 22;

function isValidEmail(x) {
    var atpos = x.indexOf("@");
    var dotpos = x.lastIndexOf(".");
    if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= x.length)
    {
        return false;
    } else {
        return true;
    }
}

function isValidPassword(pwd, confPwd) {
    if (pwd !== "" && confPwd !== "" && pwd === confPwd)
        return true;
    else
        return false;
}

function isRequired(s) {
    // Check for white space
    if (s === "" || $.trim(s) === "")
        return false;
    else
        return true;
}

/*
 * This function is used to get parameter from address bar
 * @return {String}: value is filtered on andress bar
 */
function queryString() {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

/**
 * generate paging
 * @param {type} number
 * @param {type} total
 * @param {type} link
 * @returns {String}
 */
function generatePaging(number, total, link, addmore) {
    var addmore = addmore?addmore:"";
    var total = parseInt(total);
    var number = number ? parseInt(number) : 0;
    var pageDiv = "<span> page " + (number + 1) + " of " + total + "</span>";
    var first = number === 0 ? "<span class='page-first'></span>" : "<a class='page-first' href='" + link + "?page=0"+addmore+"'></a>";
    var last = number === (total-1)? "<span class='page-last' ></span>" : "<a class='page-last' href='" + link + "?page=" + (total - 1) +addmore + "'></a>";
    var prev = number >= 1 ? "<a class='page-prev' href='" + link + "?page=" + (number - 1)+addmore + "'></a>" : "<a class='page-prev' href='#'></a>";
    var next = number <= total - 2 ? "<a class='page-next' href='" + link + "?page=" + (number + 1)+addmore + "'></a>" : "<a class='page-next' href='#'></a>";
    return first + prev + pageDiv + next + last;
}
/**
 * generate paging
 * @param {type} number
 * @param {type} total
 * @param {type} link
 * @returns {String}
 */
function generatePagingShowUser(number, total, link, addmore) {
    var addmore = addmore?addmore:"";
    var total = parseInt(total);
    var number = number ? parseInt(number) : 0;
    var pageDiv = "<span> page " + (number + 1) + " of " + total + "</span>";
    var first = number === 0 ? "<span class='page-first'></span>" : "<a class='page-first' href='" + link + "&page=0"+addmore+"'></a>";
    var last = number === (total-1)? "<span class='page-last' ></span>" : "<a class='page-last' href='" + link + "&page=" + (total - 1) +addmore + "'></a>";
    var prev = number >= 1 ? "<a class='page-prev' href='" + link + "&page=" + (number - 1)+addmore + "'></a>" : "<a class='page-prev' href='#'></a>";
    var next = number <= total - 2 ? "<a class='page-next' href='" + link + "&page=" + (number + 1)+addmore + "'></a>" : "<a class='page-next' href='#'></a>";
    return first + prev + pageDiv + next + last;
}

function checkGroupValidate(arr) {
    var result = true;
    if (arr.indexOf("-1") !== -1) {
        result = false;
    }
    return result;
}

function checkDuplicate(arr) {
    var result = true;
    arr.sort();
    var last = arr[0];
    for (var i = 1; i < arr.length; i++) {
        if (arr[i] == last)
            result = false;
        last = arr[i];
    }
    return  result;
}

function formatDate(dateStr){
    var date = new Date(dateStr);
    return dateFormat(date, "YYYY.MM.DD");
}

/**
* Format date as a string
* @param date - a date object (usually "new Date();")
* @param format - a string format, eg. "DD-MM-YYYY"
*/
function dateFormat(date, format) {
    // Calculate date parts and replace instances in format string accordingly
    format = format.replace("DD", (date.getDate() < 10 ? '0' : '') + date.getDate()); // Pad with '0' if needed
    format = format.replace("MM", (date.getMonth() < 9 ? '0' : '') + (date.getMonth() + 1)); // Months are zero-based
    format = format.replace("YYYY", date.getFullYear());
    return format;
}