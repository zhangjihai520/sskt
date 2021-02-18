import React from 'react'
import fetch from 'dva/fetch';
import { Button, Input } from 'antd';
import { queryParseJson } from '../../utils/utils';

/**
 * 同步登录云平台
 */
class PlatformLogin extends React.Component {
    sendHttp() {
        document.getElementById("login").submit();
    }

    sendHttps() {
        let params = {
            sub: '098c8a01-d53d-4927-ac8b-2bc85bfb02c6',
            name: 'jiangziya',
            redrect_url: 'http://localhost:8002/Access/PlatformLogin'
        }
        fetch("/core/customlogin", {
            method: "POST",
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
            },
            body: queryParseJson(params)
        });
    }
    formSendHttp() {
        document.getElementById("login2").submit();
    }
    render() {
        return <div>
            <p>正在同步登录云平台......</p>
            {/* 请求地址 */}
            <form action="http://devsso.dledc.com/core/customlogin" method="post" id="login" target="hidden_frame">
                {/* 云平台用户Id */}
                <input name="sub" type="hidden" value="098c8a01-d53d-4927-ac8b-2bc85bfb02c6" />
                {/* 云平台用户名 */}
                <input name="name" type="hidden" value="jiangziya" />
                {/* 重定向地址 */}
                <input name="redrect_url" type="hidden" value="http://localhost:8002/Access/PlatformLogin" />
                <Button onClick={this.sendHttp}>表单发送HTTP请求-不跳转-仅同协议可用</Button>
            </form>
            <iframe name='hidden_frame' id="hidden_frame" style={{ display: 'none' }}></iframe>
            <br />
            <p>
                <Button onClick={this.sendHttps}>发送HTTPS请求-nginx转发-请求可以发出去但是未登录</Button>
            </p>
            <form action="http://devsso.dledc.com/core/customlogin" method="post" id="login2">
                {/* 云平台用户Id */}
                <input name="sub" type="text" value="098c8a01-d53d-4927-ac8b-2bc85bfb02c6" />
                {/* 云平台用户名 */}
                <input name="name" type="text" value="jiangziya" />
                {/* 重定向地址 */}
                <input name="redrect_url" type="text" value="http://localhost:8002/Access/PlatformLogin" />
                <Button onClick={this.formSendHttp}>表单发送HTTP请求-跳转</Button>
            </form>
        </div>
    }
}

export default PlatformLogin;