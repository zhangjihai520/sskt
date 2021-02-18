//微信聊天可视化组件
//依赖scrollLoader组件, 依赖指令v-emotion（实现请查看main.js）

//参数：
// width               组件宽度，默认450
// wrapBg              外层父元素背景颜色，默认#efefef
// maxHeight           展示窗口最高高度, 默认900
// contactAvatarUrl    好友头像url
// ownerAvatarUrl      微信主人头像url
// ownerNickname       微信主人昵称
// getUpperData        （必需）当滚动到上方时加载数据的方法，返回值要为Promise对象，resolve的结构同data
// getUnderData        （必需）当滚动到下方时加载数据的方法，返回值同上
// data                （必需）传入初始化数据， 结构如下：

<style scoped>
    .wxchat-container{ width: 100%; z-index: 100; left:0; top: 0; overflow: hidden; margin-top: 0;
  flex-grow: 1;
  flex-shrink: 1;
  overflow-y: auto;}
    .shadow{ position: fixed; top:0; left: 0; z-index: 100; width: 100%; height: 100%; background: #000; opacity: .2; }
    .window {box-shadow: 1px 1px 20px -5px #000; /*max-width: 450px;*/ min-width: 300px; background: #F5F5F5; margin: 0 auto; overflow: hidden; padding: 0; height: 100%;position: relative;z-index: 101;}
    button{border:0; background:none; border-radius: 0;text-align: center;}
    button{outline:none;}
    .w100{width: 100%;}
    .mt5{margin-top: 5px;}
    .mt10{margin-top: 10px;}
    .mt20{margin-top: 20px;}
    .mb10{margin-bottom: 10px;}
    .mb20{margin-bottom: 20px;}
    .fs0{font-size: 0}
    .title{background: #000; text-align: center; color:#fff; width: 100%; height: 50px; line-height: 50px; font-size: 120x;}
    .loading,.no-more{text-align: center; color: #b0b0b0; line-height: 100px;}
    .no-more{line-height: 60px;}
    .pull-right{float: right;}
    .link-line{text-decoration: underline;}
    .message{
        /*height: 100%;*/
        padding: 10px 15px;
        /*overflow-y: scroll;*/
        background-color: #F5F5F5;
    }
    .message li {
        margin-bottom: 15px;
        left:0;
        position: relative;
        display: block;
    }
    .message .time {
        margin: 10px 0;
        text-align: center;
    }
    .message .username {
        margin: 10px 0;
        text-align: left;
    }
    .message .username>span {
        display: inline-block;
        padding: 0 5px;
        font-size: 20px;
        color: #fff;
        border-radius: 2px;
        background-color: #DADADA;
    }
    .message .text {
        display: inline-block;
        position: relative;
        padding: 0 10px;
        max-width: calc(100% - 75px);
        min-height: 35px;
        line-height: 2.1;
        font-size: 25px;
        padding: 6px 10px;
        text-align: left;
        word-break: break-all;
        background-color: #fff;
        color: #000;
        border-radius: 4px;
        box-shadow: 0px 1px 7px -5px #000;
    }
    .message .privateText {
        display: inline-block;
        position: relative;
        padding: 0 10px;
        max-width: calc(100% - 75px);
        min-height: 35px;
        line-height: 2.1;
        font-size: 25px;
        padding: 6px 10px;
        text-align: left;
        word-break: break-all;
        background-color: #fff;
        color: red;
        border-radius: 4px;
        box-shadow: 0px 1px 7px -5px #000;
        font-weight: bold;
    }
    .message .pictrue {
        display: inline-block;
        position: relative;
        padding: 0 10px;
        max-width: calc(100% - 75px);
        min-height: 35px;
        line-height: 2.1;
        font-size: 25px;
        padding: 6px 10px;
        text-align: left;
        word-break: break-all;
        background-color: #fff;
        color: #000;
        border-radius: 4px;
        box-shadow: 0px 1px 7px -5px #000;
    }
    .message .avatar {
        float: left;
        margin: 0 10px 0 0;
        border-radius: 3px;
        background: #fff;
    }
    .message .time>span {
        display: inline-block;
        padding: 0 5px;
        font-size: 20px;
        color: #fff;
        border-radius: 2px;
        background-color: #DADADA;
    }
    .message .system>span{
        padding: 4px 9px;
        text-align: left;
    }
    .message .text:before {
        content: " ";
        position: absolute;
        top: 9px;
        right: 100%;
        border: 6px solid transparent;
        border-right-color: #fff;
    }
        .message .privateText:before {
        content: " ";
        position: absolute;
        top: 9px;
        right: 100%;
        border: 6px solid transparent;
        border-right-color: #fff;
    }
    .message .self {
        text-align: right;
    }
    .message .self .avatar {
        float: right;
        margin: 0 0 0 10px;
    }
    .message .self .text {
        background-color: #9EEA6A;
    }

    .message .self .text:before {
        right: inherit;
        left: 100%;
        border-right-color: transparent;
        border-left-color: #9EEA6A;
    }

    
    .message .self .privateText {
        background-color: #9EEA6A;
        font-weight: bold;
        color: red;
    }
    .message .self .privateText:before {
        right: inherit;
        left: 100%;
        border-right-color: transparent;
        border-left-color: #9EEA6A;
    }

    .message .self .username {
        margin: 10px 0;
        text-align: right;
    }
    .message .image{
        max-width: 200px;
    }
    img.static-emotion-gif, img.static-emotion {
        vertical-align: middle !important;
    }

    .an-move-left{
        left: 0;
        animation: moveLeft .7s ease;
        -webkit-animation:moveLeft .7s ease; 
    }
    .an-move-right{
        left: 0;
        animation: moveRight .7s ease;
        -webkit-animation:moveRight .7s ease; 
    }
    .bgnone{
        background: none;
    }
    @keyframes moveRight{
        0%{left:-20px; opacity: 0};
        100%{left:0; opacity: 1}
    }

    @-webkit-keyframes moveRight
    {
        0%{left:-20px; opacity: 0};
        100%{left:0px; opacity: 1}
    }
    @keyframes moveLeft{
        0%{left:20px; opacity: 0};
        100%{left:0px; opacity: 1}
    }

    @-webkit-keyframes moveLeft
    {
        0%{left:20px; opacity: 0};
        100%{left:0px; opacity: 1}
    }

    @media (max-width: 367px){
        .fzDInfo{width:82%;}
    }

</style>

<template>
    <div id ="wxchat-container" class="wxchat-container" :style="{backgroundColor: wrapBg,overflow:hidde}">
        <div class="window" id="window-view-container" :style="{maxHeight: maxHeight + 'px', width: width +'px',overflow:hidden}">
            <!-- data is empty -->
            <div class="loading" v-if="dataArray && dataArray.length==0">
                <div style="margin-top: 300px;text-align:center; font-size: 16px;">
                    <span>未查找到聊天记录</span>
                </div>
            </div>

            <!-- loading -->
            <div class="loading" v-if="dataArray.length==0">
                <div style="margin-top: 300px;">
                    <div>加载中...</div>
                </div>
            </div>

            <!-- main -->
            <ScrollLoader :minHeight="minHeight" @scroll-to-top="refresh" class="container-main" v-if="dataArray && dataArray.length>0" :style="{maxHeight: maxHeight + 'px'}">
                <div id="message" class="message" :style="{overflow:hidden}">
                    <ul>
                        <li v-for="message in dataArray" :key="message.TalkMessageId" :class="message.UserId == ownerUserId ? 'an-move-right':'an-move-left'">
                            <p class="time"> <span v-text="process(message.ShowDateTime)"></span> </p>
                            <!--<p class="time system" v-if="message.type==10000"> <span v-html="message.content"></span> </p>-->
                            <div :class="'main' + (message.UserId == ownerUserId ? ' self':'')">
                                <img class="avatar" width="40" height="40" :src="message.UserFace">
                                <div>
                                    <div class="username" > <span v-text="message.UserTrueName"></span> </div>
                                    <!-- 文本 -->
                                    <div :class="message.ToUserId > 0 ? 'privateText':'text'"  v-emotion="message.ToUserId > 0 ? '私信：' + message.Content : message.Content" v-if="message.TalkTypeId == 1"></div>

                                    <!-- 图片 -->
                                    <div :class="message.ToUserId > 0 ? 'privateText':'text'" v-else-if="message.TalkTypeId == 2">
                                        <!--<img :src="message.ThumbImageUrl" class="image" alt="聊天图片">-->
                                        <img :src="message.ThumbImageUrl" @click="seeBigTu(message.AttachmentUrl)">
                                    </div>

                                    <!-- 其他 -->
                                    <div :class="message.ToUserId > 0 ? 'privateText':'text'" v-else v-text="'[暂未支持的消息类型]'">
                                    
                                    </div>
                                </div>
                            </div>
                        </li>
                        
                    </ul>
                </div>
            </ScrollLoader>
        </div>
    </div>
</template>

<script>
    import ScrollLoader from './scrollLoader.vue';
    export default {
        name: "wxChat",

        components: {
            ScrollLoader
        },

        props: {
            data: {
                type: Array,
                required: true
            },

            width: {
                type: Number,
                default: 450,
            },
            isIos:{
                type: Boolean,
                default: false,
            },

            wrapBg: {
                type: String,
                default: '#efefef'
            },

            maxHeight: {
                type: Number,
                default: 700
            },

            ownerUserId: {
                type: Number,
                default: 0,
            },

            getUpperData: {
                type: Function,
                required: true
            },
        },

        data() {
            return {
                isUpperLaoding: false,

                isRefreshedAll: false,
                isLoadedAll: false,
                
                minHeight: 700,
                dataArray: [],
                beforeHight:0
            }
        },

        created() {
            this.initData();
        },
        mounted(){
            //document.getElementsByTagName('body')[0].scrollTop=0;
            this.minHeight = document.getElementById('window-view-container').offsetHeight;
            this.maxHeight = document.getElementById('window-view-container').offsetHeight;
            this.scrollToBottom();
        },
        methods: {
            process(date){
                if(date)
                {
                    return new Date(date).toLocaleString()
                }else{
                    return "";
                }
                
            },
            initData(){
                this.dataArray = this.dataArray.concat(this.data);
                if(this.dataArray.length>100)
                {
                    this.dataArray = this.dataArray.slice(50,nowPane.messages.length + 1)
                    this.$emit("changeUpperId",this.dataArray[0].TalkMessageId)
                }
                this.scrollToBottom()
            },

            seeBigTu(value) {    
                WeixinJSBridge.invoke('imagePreview', {
                    'urls': [value],
                    'current': value
                });     
            },
            //向上拉刷新
            refresh(done) {
                var me = this;
                if(me.isUpperLaoding){
                    return;
                }else{
                    me.isUpperLaoding = true;
                }

                if(me.isRefreshedAll){
                    done(true);
                    me.isUpperLaoding = false;
                    return;
                }
                
                try {
                    this.getUpperData().then(function(result){
                        if(!result.TalkMessageList||result.TalkMessageList.length==0){
                            me.isRefreshedAll = true;
                            done(true);
                        }else{
                            me.$emit("changeUpperId",result.TalkMessageList[0].TalkMessageId)
                            me.dataArray = result.TalkMessageList.concat(me.dataArray); //倒序合并
                            done();
                            var scrollContainer = document.getElementById('scrollLoader-container');
                            var scrollContent = document.getElementById('scrollContent');
                            if(me.isIos)
                            {
                                scrollContainer.scrollTop = scrollContainer.scrollHeight - me.beforeHight
                                me.beforeHight = scrollContainer.scrollHeight;
                            }else{
                                scrollContainer.scrollTop = scrollContainer.scrollHeight - scrollContent.offsetHeight;
                                var wxchatScrollContainer = document.getElementById('wxchat-container');
                                wxchatScrollContainer.scrollTop = wxchatScrollContainer.scrollHeight;
                            }
                        }
                        me.isUpperLaoding = false;
                    })
                } catch (error) {
                    console.error('wxChat: props "getUpperData" must return a promise object!')
                }
                
            },
            scrollToBottom: function () {
                this.$nextTick(() => {
                    var scrollContainer = document.getElementById('scrollLoader-container');
                    scrollContainer.scrollTop = scrollContainer.scrollHeight;
                    var wxchatScrollContainer = document.getElementById('wxchat-container');
                    wxchatScrollContainer.scrollTop = wxchatScrollContainer.scrollHeight;
                });
            }

        },
        watch:{     //监听value的变化，进行相应的操作即可
            immediate:true,
            data: async function(a,b){     //a是value的新值，b是旧值
                this.dataArray = this.dataArray.concat(a);
                if(this.dataArray.length>100)
                {
                    this.dataArray = this.dataArray.slice(this.dataArray.length-50,this.dataArray.length + 1)
                    this.$emit("changeUpperId",this.dataArray[0].TalkMessageId)
                }
                if(b.length==0||this.dataArray<=20||a[0].UserId==this.ownerUserId){
                    this.scrollToBottom();
                }else{
                    var scrollContainer = document.getElementById('scrollLoader-container');
                    if(scrollContainer.scrollHeight-scrollContainer.scrollTop-scrollContainer.offsetHeight < 700)
                    {
                        this.scrollToBottom();
                    }
                }
                 
            }
        }
    }
</script>