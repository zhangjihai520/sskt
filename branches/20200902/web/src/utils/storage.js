import Cookies from 'js-cookie';
const PREFIX = 'SSKT';

let ls = window.localStorage;
let ss = window.sessionStorage;

/**
 * 获取cook
 */
export function getTokenByCookie() {
	return getCookie('Authorization');
}

/**
 * 存储Cook
 * @param {*} token
 */
export function setToken(token) {
	setCookie('Authorization', token);
}

/**
 * 获取cookie
 */
export function getCookie(cookName) {
	return Cookies.get(cookName);
}

/**
 * 删除指定name cookie
 * @param {*} cookName
 */
export function delCookie(cookName = 'Authorization') {
	Cookies.remove(cookName);
}

/**
 * 清空所有cookie
 */
function clearCookie() {
	const keys = document.cookie.match(/[^ =;]+(?=\=)/g);
	if (keys) {
		for (let i = keys.length; i--; ) Cookies.remove(keys[i]);
	}
}

/**
 * 设置cook，储存7天
 */
export function setCookie(cookName, value) {
	delCookie(cookName);
	Cookies.set(cookName, value, { expires: 7, path: '/' });
}

/**
 * 设置cookie过期时间跟随浏览器进程
 */
export function setNoExpiresCookie(cookName, value) {
	Cookies.set(cookName, value);
}

/*
* 根据key获取value
 * @param {*} key
 */
export function getLocalStorage(key) {
	const value = ls.getItem(`${PREFIX}-${key.toUpperCase()}`);
	return value === null ? null : JSON.parse(ls.getItem(`${PREFIX}-${key.toUpperCase()}`));
}

/**
 * 存储数据到localStorage
 * @param {*} key
 * @param {*} value
 */
export function setLocalStorage(key, value) {
	ls.setItem(`${PREFIX}-${key.toUpperCase()}`, JSON.stringify(value));
}

/**
 * 删除指定的localStorage
 * @param {*} key
 */
export function removeLocalStorage(key) {
	ls.removeItem(`${PREFIX}-${key.toUpperCase()}`);
}

export function clearAllStorage() {
	ss.clear();
	clearCookie();
}

export function clearSessionStorage() {
	ss.clear();
}

export function getSessionStorage(key) {
	const value = ss.getItem(`${PREFIX}-${key.toUpperCase()}`);
	return value === null ? null : JSON.parse(ss.getItem(`${PREFIX}-${key.toUpperCase()}`));
}

export function setSessionStorage(key, value) {
	ss.setItem(`${PREFIX}-${key.toUpperCase()}`, JSON.stringify(value));
}

export function removeSessionStorage(key) {
	ss.removeItem(`${PREFIX}-${key.toUpperCase()}`);
}
