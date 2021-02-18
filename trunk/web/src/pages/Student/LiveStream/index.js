import React, { Component } from 'react';
import { Button, Input, message, Tabs, Popover, Collapse, Row, Col, Badge,Modal, Spin, Empty } from 'antd';
import $ from 'jquery';
import style from './index.less';
import { connect } from 'dva';
import { getUrlQuery } from 'utils/utils';
import imgEmojImg from 'assets/common/send_emoj2.png';
import imgUploadImg from 'assets/common/send_img2.png';
import imgUploadFile from 'assets/common/send_file2.png';
import imgExcel from 'assets/common/img_excel.png';
import imgPdf from 'assets/common/img_pdf.png';
import imgPPT from 'assets/common/img_ppt.png';
import imgWord from 'assets/common/img_word.png';
import SwitchIcon from 'assets/common/switch_icon.png';
import answercardIcon from 'assets/common/answercard.png'
import DiscordAvotor from 'assets/common/discord_avotor.png';
import { colorArr, emojiArr } from 'config/enum';
import classnames from 'classnames';
import _ from 'lodash';
import Zmage from 'react-zmage'
import Player from 'xgplayer'
import FlvJsPlayer from 'xgplayer-flv.js';
import HlsJsPlayer from 'xgplayer-hls.js';

import LiveTopicList from "../../../components/LiveTopicList";
import LiveNumberKeyboard from "../../../components/LiveNumberKeyboard";
const confirm = Modal.confirm;
const { TextArea } = Input;
const { TabPane } = Tabs;
const { Panel } = Collapse;
let player;
let ws;
let timeoutObj;
const initialPanes = [{title: '互动区', key: '0', messages: [], closable: false, scrollActive: true}];
@connect(({ loading,commonModel }) => ({
  listLoading: loading.effects["curriculumModel/GetExamSetPreview"],
  studentBasePath: commonModel.StudentBasePath,
  CurUserSystem: commonModel.CurUserSystem
}))
export default class ListenStream extends Component {
  constructor(props) {
    super(props);
    this.state = {
      RoomUserType: 0,
      UserId: 0,
      lockReconnect: false,
      noTalkingChecked: false,
      wsUrl: '',
      message: '',
      data: {},
      activeKey: initialPanes[0].key,
      panes: initialPanes,
      loading: false,
      tarVideoStream: 2,
      playTimeZone: 0,
      userInfoList: [],
      colorData: colorArr,
      playerReady: false,
      emojiData: emojiArr,
      emotionVisible: false,
      UserStatus: 0,
      unReadKey:[],
      beforeScrollHight:0,

      answerList: [],
      topicListData: {},
      serialNumber: 1,
      cardVisible:false,
      ExamSetStatus:0,
      IsFinish:false
    };
  }

  componentDidMount() {
    this.getSource();
    $('.gd').css({ width: '100%' });
  }

  componentWillUnmount() {
    $('.gd').css({ width: '' });
    this.reset();
    if (ws) {
      ws.close();
    }
    if(player)
    {
      player.destroy();
      player=null;
    }
  }

  initWebSocket = () => {
    const self = this;
    ws = new WebSocket(this.state.wsUrl);
    if (typeof (ws) == "undefined") {
      console.log("您的浏览器不支持WebSocket");
    } else {
      console.log("您的浏览器支持WebSocket");
      ws.onopen = (evt) => {
        console.log(this.state.wsUrl + " Connection open ...");
        this.reset();
        this.start();
      };
      ws.onmessage = (evt) => {
        this.setState({ lockReconnect: true });
        if (evt.data == "HeartBeat") {
          return;
        }
        if (evt.data == "OpenExam") {
          this.setState({ ExamSetStatus: 1});
          return;
        }
        let obj = JSON.parse(evt.data);
        if (obj.TalkMessage && obj.MessageType != 1) {
          const newPanes = [...this.state.panes];
          const paneKey = (obj.TalkMessage.ToUserId > 0 ? (this.state.UserId == obj.TalkMessage.UserId ? obj.TalkMessage.ToUserId : obj.TalkMessage.UserId) : 0) + "";
          const nowPane = this.state.panes.find(pane => pane.key == paneKey);
          if(nowPane)
          {
            var paneIndex = this.state.panes.findIndex(pane => pane.key == paneKey);
            var newMessages = [];
            if(obj.MessageType == 0)
            {
              newMessages = [...nowPane.messages,obj.TalkMessage];
            }else{
              newMessages = nowPane.messages.filter(item => item.TalkMessageId != obj.TalkMessage.TalkMessageId)
            }
            newPanes.splice(paneIndex, 1, { title: nowPane.title,messages: newMessages, key: nowPane.key, closable: nowPane.closable, scrollActive: nowPane.scrollActive});
            this.setState({panes: newPanes,userInfoList: obj.UserInfoList},()=>{
              if(nowPane.scrollActive)
              {
                if(nowPane.messages.length>100)
                {
                  newPanes.splice(paneIndex, 1, { title: nowPane.title,messages: nowPane.messages.slice(nowPane.messages.length-50,nowPane.messages.length+1), key: nowPane.key, closable: nowPane.closable, scrollActive: nowPane.scrollActive});
                  this.setState({ panes: newPanes});
                }
                this.handleToNewMessage(paneKey,0);
              }
            });
          }
          var unReadKeyIndex = this.state.unReadKey.findIndex(key => key == paneKey);
          if(unReadKeyIndex==-1 && this.state.activeKey !=paneKey)
          {
            this.setState({unReadKey: [...this.state.unReadKey,paneKey]});
          }
          if (obj.TalkMessage.TalkTypeId == 1 && obj.TalkMessage.Content.indexOf('**') == -1) {
            obj.TalkMessage.Content = obj.TalkMessage.Content.replace(/(\r\n|\n|\r)/gm, '<br />');
            if(obj.TalkMessage.ToUserId == 0 && this.state.playerReady){
              var startTime = new Date().getTime() - this.state.playTimeZone;
              // player.danmu.sendComment({
              //   duration: 10000, //弹幕持续显示时间,毫秒(最低为5000毫秒)
              //   id: obj.TalkMessage.TalkMessageId + '', //弹幕id，需唯一
              //   start: startTime, //弹幕出现时间，毫秒
              //   color: true, //该条弹幕为彩色弹幕，默认false
              //   txt: obj.TalkMessage.UserTrueName + ":" + obj.TalkMessage.Content, //弹幕文字内容
              //   style: {  //弹幕自定义样式
              //     color: self.getColor(),
              //     fontSize: '20px',
              //     padding: '3px 6px',
              //   }
              // });
            }
          }
        }
        if(obj.UserInfoList)
        { 
          this.setState({ userInfoList: obj.UserInfoList });
          obj.UserInfoList.forEach(item => {
            if(item.UserId == this.state.UserId)
            {
              this.setState({
                UserStatus: item.Status
              });
              if(item.Status > 0)
              {
                this.setState({
                  emotionVisible: false
                });
              }
            }
          });
        }
      };
      ws.onclose = (evt) => {
        console.log("Connection closed.");
        this.setState({ lockReconnect: false });
        ws.close()
      };
      ws.onerror = (evt) => {
        this.setState({ lockReconnect: false });
      };
      window.onbeforeunload = (event) => {
        console.log("关闭WebSocket连接！");
      }
    }
  }

  getColor = () => {
    return this.state.colorData[Math.floor(Math.random() * this.state.colorData.length)];
  }

  reset = () => {
    clearInterval(timeoutObj);
    return this;
  }

  start = () => {
    const self = this;
    timeoutObj = setInterval(function () {
      //这里发送一个心跳，后端收到后，返回一个心跳消息，
      //onmessage拿到返回的心跳就说明连接正常
      if (ws.readyState === 1) {
        ws.send("HeartBeat");
      }
      if (!self.state.lockReconnect) {
        self.reconnect();
      }
    }, 60000)
  }

  reconnect = () => {
    const self = this;
    if (this.state.lockReconnect) return;
    this.setState({ lockReconnect: true });
    //没连接上会一直重连，设置延迟避免请求过多
    setTimeout(function () {
      if (ws.readyState === 2 || ws.readyState === 3) {
        self.reset();
        self.initWebSocket();
        self.setState({ lockReconnect: false });
      }
    }, 2000);
  }

  getSource = () => {
    const SubjectId = getUrlQuery(window.location.href, 'SubjectId');
    this.SubjectId = SubjectId;
    this.props
      .dispatch({
        type: 'stuModel/GetSubjectVideo',
        payload: {
          SubjectId: SubjectId
        }
      })
      .then(result => {
        if (result.SubjectStatus != 5) {
          this.setState(
            {
              data: result,
              IsFinish: result.IsFinish
            },
            () => {
              this.createPlayer(this.state.data.VideoUrl[0] ? this.state.data.VideoUrl[0] : this.state.data.PPTVideoUrl[0]);
            }
          );
        } else {
          this.setState(
            {
              data: result
            },
            () => {
              this.createPlayer('');
            }
          );
        }
        let protocol = location.protocol === 'https:' ? 'wss' : 'ws';
        // let url = protocol + "://192.168.50.17:8080/api/chat/" + result.Channel + "/" + result.Uid + "_web";
        let url = protocol+"://sskt.nceduc.cn/api/chat/"+result.Channel+"/"+result.Uid+ "_web";
        this.setState(
          {
            UserId: result.Uid,
            wsUrl: url
          }, () => {this.getMessage(0);this.initWebSocket();this.getClassWork()});
        
      });
  };

  getMessage = (toUserId) => {
    const self = this;
    const key = toUserId + "";
    var nowPane = this.state.panes.find(pane => pane.key == key);
    const BoundaryId = nowPane && nowPane.messages.length > 0 ? Number(nowPane.messages[0].TalkMessageId) : 0;
    this.props
      .dispatch({
        type: 'stuModel/Message',
        payload: {
          SubjectId: this.SubjectId,
          BoundaryId: BoundaryId,
          ToUserId: toUserId,
          TypeId: BoundaryId > 0 ? 1 : 3,
          PageSize: 20
        }
      })
      .then(result => {
        this.setState(
          {
            noTalkingChecked: result.RoomStatus > 0,
            RoomUserType: result.RoomUserType,
            ExamSetStatus: result.ExamSetStatus
          });
        result.TalkMessageList.forEach(item => {
          if (item.TalkTypeId == 1) {
            item.Content = item.Content.replace(/(\r\n|\n|\r)/gm, '<br />');
          }
        });
        const newPanes = [...this.state.panes];
        var paneIndex = this.state.panes.findIndex(pane => pane.key == key);
        newPanes.splice(paneIndex, 1, { title: nowPane.title,messages: result.TalkMessageList.concat(nowPane.messages), key: nowPane.key, closable: nowPane.closable, scrollActive: nowPane.scrollActive});
        this.setState({  panes: newPanes, userInfoList: result.UserInfoList},()=>{
            this.handleToNewMessage(key,1);
        });
    });
  };

  createPlayer = source => {
    const self = this;
    if (player) {
      player.destroy();
      $('#player-con').empty();
      player = null;
    }
    let option = {
      id: 'player-con',
      width: '646px',
      height: '402px',
      autoplay: true,
      videoInit: true,
      lang: 'zh-cn',
      volume: 1,
      fitVideoSize: 'auto',
      loop: false,
      ignores: ['play', 'time', 'progress', 'replay'],
      url: source,
      playsinline: true,
      closeVideoClick: true,
      closeVideoDblclick: true,
      closeVideoTouch: true,
      danmu: {
        comments: [
        ],
        area: {  //弹幕显示区域
          start: 0, //区域顶部到播放器顶部所占播放器高度的比例
          end: 1 //区域底部到播放器顶部所占播放器高度的比例
        },
      },
      whitelist: [
        ""
      ]
    };
    $(function () {
      if (source && source.indexOf('.mp4') > -1) {
        player = new Player(option);
      } else if (source && source.indexOf('.m3u8') > -1) {
        player = new HlsJsPlayer(option);
      } else {
        player = new FlvJsPlayer(option);
      }
      player.once('playing', function (deg) {
        self.setState({
          playTimeZone: new Date().getTime()
        });
      });
      player.once('ready',()=>{
        self.setState({
          playerReady: true
        });
      });
      if (option.autoplay) {
        player.reload();
        self.setState({
          playTimeZone: new Date().getTime()
        });
      }
    });
  };

  handleSwitch = targetIndex => {
    if (targetIndex == 1) {
      this.createPlayer(this.state.data.VideoUrl[0] ? this.state.data.VideoUrl[0] : this.state.data.PPTVideoUrl[0]);
      this.setState({
        tarVideoStream: 2
      });
    } else {
      this.createPlayer(this.state.data.PPTVideoUrl[0]);
      this.setState({
        tarVideoStream: 1
      });
    }
  };

  uploadImage = e => {
    let currentTarget = e.target;
    const file = currentTarget.files[0];
    if (!file) {
      return;
    }
    const imgMasSize = 1024 * 1024 * 3;
    currentTarget.value = '';

    // check extension
    if (['jpeg', 'png', 'jpg'].indexOf(file.type.split('/')[1]) < 0) {
      message.destroy();
      message.error('图片格式支持JPG、JPEG、PNG格式');
      return;
    }

    // check file size
    if (file.size > imgMasSize) {
      message.destroy();
      message.error('图片大小不超过3M');
      return;
    }

    this.upload(file, 2);
  };

  uploadAffix = e => {
    let currentTarget = e.target;
    const file = currentTarget.files[0];
    if (!file) {
      return;
    }
    const imgMasSize = 1024 * 1024 * 20;
    currentTarget.value = '';

    // check extension
    if (
      [
        'vnd.ms-powerpoint',
        'vnd.openxmlformats-officedocument.presentationml.presentation',
        'pdf',
        'msword',
        'vnd.openxmlformats-officedocument.wordprocessingml.document',
        'vnd.openxmlformats-officedocument.spreadsheetml.sheet',
        'vnd.ms-excel'
      ].indexOf(file.type.split('/')[1]) < 0
    ) {
      message.destroy();
      message.error('附件格式支持ppt、pptx、pdf、doc、docx、xlsx、xls格式');
      return;
    }

    // check file size
    if (file.size > imgMasSize) {
      message.destroy();
      message.error('附件大小不超过20M');
      return;
    }

    this.upload(file, 3);
  };

  upload = (file, type) => {
    // create formData
    const formData = new FormData();
    formData.append('SubjectId', this.SubjectId);
    formData.append('FileType', type);
    formData.append('file', file);
    formData.append('ToUserId', Number(this.state.activeKey))
    this.props
      .dispatch({
        type: 'stuModel/SendFile',
        payload: formData
      });
    this.handleToNewMessage(this.state.activeKey,0);
  };

  handleAnswerChange = e => {
    this.setState({ message: e.target.value });
  };

  handleSubmitMessage = () => {
    const self = this;
    self.setState({ message: '' });
    if (this.state.message.trim() == '') {
      return;
    }
    this.props
      .dispatch({
        type: 'stuModel/SendMessage',
        payload: {
          SubjectId: this.SubjectId,
          ToUserId: Number(this.state.activeKey),
          MessageContent: this.state.message
        }
      }).then(result => {
        const newPanes = [...this.state.panes];
        var nowPane = this.state.panes.find(pane => pane.key == this.state.activeKey);
        var paneIndex = this.state.panes.findIndex(pane => pane.key == this.state.activeKey);
        newPanes.splice(paneIndex, 1, { title: nowPane.title, messages:  nowPane.messages, key: nowPane.key, closable: nowPane.closable, scrollActive: true});
        this.setState({panes: newPanes});
        this.handleToNewMessage(this.state.activeKey,0);
      });
  };

  handlActive = key => {
    const newPanes = [...this.state.panes];
    var nowPane = this.state.panes.find(pane => pane.key == key);
    var paneIndex = this.state.panes.findIndex(pane => pane.key == key);
    newPanes.splice(paneIndex, 1, { title: nowPane.title, messages: nowPane.messages, key: nowPane.key, closable: nowPane.closable, scrollActive: false});
    this.setState({panes: newPanes});
  };

  handleScroll = key => {
    const newPanes = [...this.state.panes];
    var atv = false;
    var id = '#wrap_' + key;
    if($(id).scrollTop()==0)
    {
      this.getMessage(Number(key));
    }
    if ($(id).prop('scrollHeight')-($(id).scrollTop() + $(id).outerHeight())<400) {
      atv = true;
    } else {
      atv = false;
    }
    var nowPane = this.state.panes.find(pane => pane.key == key);
    var paneIndex = this.state.panes.findIndex(pane => pane.key == key);
    newPanes.splice(paneIndex, 1, { title: nowPane.title, messages:  nowPane.messages, key: nowPane.key, closable: nowPane.closable, scrollActive: atv});
    this.setState({panes: newPanes});
  };

  handleToNewMessage = (key,type) => {
    var id = '#wrap_' + key;
    var scrollHeight = $(id).prop('scrollHeight');
    if(type==1)
    {
      $(id).scrollTop(scrollHeight-this.state.beforeScrollHight);
    }else{
      $(id).scrollTop(scrollHeight);
    }
    this.setState({beforeScrollHight: scrollHeight});
  };

  handlePressEnter = e => {
    if (e.key === 'Enter' && e.shiftKey != true) {
      e.preventDefault();
      this.handleSubmitMessage();
    }
  };

  handleEmojiVisibleChange = visible => {
    if(this.state.UserStatus == 0)
    {
      this.setState({emotionVisible:visible });
    }
  };

  handleCardVisibleChange = visible => {
      this.setState({cardVisible:visible });
  };
  addEmoji = item =>{
    if(this.state.UserStatus == 0)
    {
      this.setState({ message: this.state.message + item,emotionVisible:false });
    }
  };

  toEmotion = (text) => {
    if (!text) {
      return text;
    }

    text = text.replace(/(\[[\u4E00-\u9FA5]{1,3}\]|(\[OK\])|(\[NO\]))/gi, function (word) {
      var newWord = word.replace(/\[|\]/gi, '');
      var index = emojiArr.indexOf(newWord);
      var backgroundPositionX = -index * 24
      var imgHTML = '';
      if (index < 0) {
        return word;
      }
      var path = index > emojiArr.length ? '/img' : 'https://res.wx.qq.com/mpres/htmledition/images/icon';
      imgHTML = `<img class="static-emotion-gif" title=${newWord} style=${{verticalAlign: 'middle'}} src="${path}/emotion/${index}.gif">`;
      return imgHTML;
    });
    return text;
  };

  switchCard = activeKey => {
    this.setState({
      activeKey
    },()=>{this.readKey(activeKey);});
  };

  readKey = readKey => {
    const newUnReadKey = this.state.unReadKey.filter(key => key !== readKey);
    this.setState({
      unReadKey: newUnReadKey
    });
    const nowPane = this.state.panes.find(pane => pane.key == readKey);
    if(nowPane && nowPane.scrollActive)
    {
      this.handleToNewMessage(readKey,0);
    }
  };


  onEdit = (targetKey, action) => {
    this[action](targetKey);
  };


  add = obj => {
    if(obj.UserId == this.state.UserId)
    {
      return;
    }
    const { panes } = this.state;
    const activeKey = obj.UserId+"";
    const nowPanes = panes.filter(pane => pane.key == activeKey);
    if(nowPanes.length==0)
    {
      const newPanes = [...panes];
      newPanes.push({ title: obj.UserName, messages:[], key: activeKey, closable: true, scrollActive: true});
      this.setState({
        panes: newPanes,
        beforeScrollHight: 0,
        activeKey
        
      },()=>{this.getMessage(obj.UserId);});
      this.readKey(activeKey);
    }else{
      this.switchCard(activeKey);
    }
  };

  remove = targetKey => {
    const { panes, activeKey } = this.state;
    let newActiveKey = activeKey;
    let lastIndex;
    panes.forEach((pane, i) => {
      if (pane.key === targetKey) {
        lastIndex = i - 1;
      }
    });
    const newPanes = panes.filter(pane => pane.key !== targetKey);
    if (newPanes.length && newActiveKey === targetKey) {
      if (lastIndex >= 0) {
        newActiveKey = newPanes[lastIndex].key;
      } else {
        newActiveKey = newPanes[0].key;
      }
    }
    this.setState({
      panes: newPanes,
      activeKey: newActiveKey,
    },()=>{this.readKey(newActiveKey);});
  };

  handleGoExcise = () => {
    const { data} = this.state;
    const { studentBasePath } = this.props;
    if (data.ExamSetId) {
      window.open(`${studentBasePath}/CourseManager/ViewAssessment?ExamSetId=${data.ExamSetId}`, '_blank');
    } else {
      message.destroy();
      message.info('当前课程未布置作业');
    }
  };

  
  getClassWork =()=>{
    const { data} = this.state;
    this.props.dispatch({
			type: "curriculumModel/GetExamSetPreview",
			payload: {
				ExamSetId: data.ExamSetId
			}
		}).then((res) => {
			this.setState({
				topicListData: res
			});
		});
  }
  //题目列表选中
  topicListChange = (answerList, number) => {
    this.setState({
      answerList,
      serialNumber: number
    });
  };
  //根据题目序号切换
  changeNumber = (num) => {
    this.setState({
      serialNumber: num
    });
  };
  //点击提交按钮
  submit = () => {
    const self = this;
    const { answerList = [], topicListData = {} } = this.state;
    //判断是不是每题都做了
    if (answerList.length === topicListData.Questions.length) {
      self.submitFn();
    } else {
      confirm({
        title: "还有未作答的题目",
        content: "",
        okText: "坚持提交",
        cancelText: "继续作答",
        onOk() {
          self.submitFn();
        }
      });
    }
  };
  //提交接口方法
  submitFn = () => {
    const { answerList = [],data } = this.state;
    this.props.dispatch({
      type: "curriculumModel/SubmitExamSet",
      payload: {
        ExamSetId: data.ExamSetId,
        QuestionList: answerList
      }
    }).then((res) => {
      if (res === 1) {
        message.success("提交成功", 1.5).then(() => {
          this.setState(
            {
              IsFinish: true
            })
        });
      } else {
        message.success("提交失败");
      }
    });
  };
  render() {
    const { data, message, tarVideoStream, UserStatus, userInfoList, emojiData, panes, activeKey, unReadKey,topicListData = {}, answerList = [], serialNumber, ExamSetStatus, IsFinish} = this.state;
		const { listLoading } = this.props;
		const numLength = topicListData.Questions && topicListData.Questions.length;
    const onlineCount = _.filter(userInfoList, item => {
      return item.IsOnline == true;
    }).length,
    peopleCount = userInfoList.length;

    const messContent = item => {
      switch (item.TalkTypeId) {
        case 1:
          return (
            <div className={style.mess}>
              <div className={style.normal} dangerouslySetInnerHTML={{ __html: this.toEmotion(item.Content) }}></div>
            </div>
          );
          break;
        case 2:
          return (
            <div className={style.mess}>
              <Zmage className={style.image} src={item.AttachmentUrl} />
            </div>
          );
          break;
        case 3:
          const AffixExtent = item.AttachmentUrl.substring(item.AttachmentUrl.lastIndexOf('.') + 1).toLowerCase();
          let affixImgSrc = '';
          if (AffixExtent.indexOf('ppt') > -1) {
            affixImgSrc = imgPPT;
          } else if (AffixExtent.indexOf('xls') > -1) {
            affixImgSrc = imgExcel;
          } else if (AffixExtent.indexOf('pdf') > -1) {
            affixImgSrc = imgPdf;
          } else if (AffixExtent.indexOf('doc') > -1) {
            affixImgSrc = imgWord;
          }
          return (
            <div className={style.mess}>
              <div className={style.affix}>
                <img className={style.affixImg} src={affixImgSrc} />
                <div className={style.desc}>
                  <div className={style.filename}>
                    <a href={item.AttachmentUrl} target="_blank">
                      {item.Content}
                    </a>
                  </div>
                  <div className={style.filesize}>{item.FileSize}</div>
                </div>
              </div>
            </div>
          );
          break;
      }
    };
    const process = date => {
      if (date) {
        return new Date(date).toLocaleString()
      } else {
        return "";
      }

    }

    const member = item => {
      return (
        <div className={item.IsOnline ? (item.Status == 1 ? style.noTokingUser : (item.Status == 2 ? style.blackUser : style.name)) : classnames(style.name, style.off)}>
          {item.UserName}
        </div>
      );
    };

    const emotion =()=> {
      return (
        <div className={style.emotionList}>
          {emojiData && emojiData.map((item, index) => {
            return <img key={index} src={`https://res.wx.qq.com/mpres/htmledition/images/icon/emotion/${index}.gif`} onClick={() => this.addEmoji(`[${item}]`)} />
          })}
        </div>
      )
    }
    const answerSheet=()=>{
      return (
        <div className={style.card}>
          <LiveNumberKeyboard
            numLength={numLength}
            currentNum={serialNumber}
            onChange={this.changeNumber}
            onSubmit={this.submit}
          />
        </div>
      )
    }

    const tabTitle = pane =>{
      return (
        <Badge dot={pane.key != activeKey && unReadKey.findIndex(key => key == pane.key) > -1}>
          <span>{pane.title}</span>
        </Badge>
      )
    }
    const classWorkContent = () =>{
      if(data.ExamSetId)
      {
        if(ExamSetStatus > 0)
        {
          if(IsFinish > 0)
          {
            return (<div className={style.emptyContent}>
              <Button onClick={() => {this.handleGoExcise();}}>查看测评</Button>
            </div>
            )
          }else{
            return (
              <div id="classWork" className={style.bottom}>
                <Spin tip="Loading..." spinning={listLoading}>
                  <LiveTopicList
                    topicListData={topicListData}
                    answerList={answerList}
                    onChange={this.topicListChange}
                    onSubmit={this.submit}
                    currentIndex={serialNumber}
                  />
                </Spin>
              </div>
            )
          }
        }else{
          return (
            <div className={style.emptyContent}>
              <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description="随堂作业待开启" />
            </div>
          )
        }
      }else{
        return (
          <div className={style.emptyContent}>
            <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} description="未布置随堂作业" />
          </div>
        )
      }
    }
    return (
      <div className={style.contentArea}>
        <Row className={style.contentTop}>
          <span>课程名称：{data.SubjectName}</span>
          <span className={style.teacher}>主讲：{data.TeacherName}</span>
        </Row>
        <hr />
        <Row className={style.videoPanel}>
          {data.ExamSetId && ExamSetStatus > 0 && !IsFinish &&(
            <div className={style.leftButtomPanel} >
              <Popover className={style.triggerDesc} placement="rightTop" visible={this.state.cardVisible} content={answerSheet()} onVisibleChange={this.handleCardVisibleChange} trigger="click"> 
                <div>
                  <img src={answercardIcon} title="答题卡" />
                </div>
              </Popover>
              <div style={{marginTop:'200px'}} >
                <Button onClick={()=> {this.submit();}} className={style.btn}>提交</Button>
              </div>
            </div>
          )}
          {data.VideoUrl && data.PPTVideoUrl && data.VideoUrl[0] != null && data.PPTVideoUrl[0] != null && (
            <div className={style.leftTopPanel}>
              <div onClick={() => {this.handleSwitch(tarVideoStream);}}>
                <img src={SwitchIcon} title="切换画面" />
              </div>
            </div>
          )}
          
          <Col flex="646px" className={style.video_leftPanel} >
            <div className={style.top} id="player-con"></div>
            {classWorkContent()}
          </Col>

          <Col flex="auto" className={style.video_center}>
            <Tabs type="editable-card" onChange={this.switchCard} activeKey={activeKey} onEdit={this.onEdit}  hideAdd={true} defaultActiveKey="0" className={style.tabs}>
              {panes.map(pane => (
                <TabPane tab={tabTitle(pane)} key={pane.key} closable={pane.closable}>
                  <div id={`wrap_${pane.key}`} className={style.detaiWrap} onClick={()=>this.handlActive(pane.key)} onScroll={()=>this.handleScroll(pane.key)}>
                    {pane.messages &&
                      pane.messages.map((item, index) => {
                        return (
                          <div key={item.TalkMessageId} className={item.UserId == this.state.UserId ? style.anMoveRight : style.anMoveLeft}>
                            <p style={{ textAlign: 'center' }}> <span >{process(item.ShowDateTime)}</span> </p>
                            <div className={style.messageItemWrap} key={`message${index}`}>
                              <div className={style.avator}>
                                <img src={item.UserFace ? item.UserFace : DiscordAvotor} />
                              </div>
                              <div className={style.item}>
                                <div className={style.name}>{item.UserTrueName}</div>
                                {messContent(item)}
                              </div>
                            </div>
                          </div>
                        );
                      })}
                  </div>
                </TabPane>
              ))}
            </Tabs>
            <div className={style.inputWrap}>
                  <TextArea
                    disabled={UserStatus > 0}
                    className={style.messInput}
                    maxLength={300}
                    onChange={this.handleAnswerChange}
                    onKeyPress={this.handlePressEnter}
                    value={message}
                    placeholder={UserStatus > 0 ? "禁言中" : "请输入内容" }
                  />
                  <div className={style.newBlockLine}></div>
                  <div className={style.newAffixUploadWrap}>
                    <div className={style.fileInput} >
                      <Popover  className={style.triggerDesc} placement="top" visible={this.state.emotionVisible} content={emotion()} onVisibleChange={this.handleEmojiVisibleChange} trigger="click">
                        <div key='1'>
                          <img src={imgEmojImg} />
                          <span className={style.triggerName}>表情</span>
                        </div>
                      </Popover>
                    </div>
                    {/* <div className={style.fileInput} style={{ marginLeft: '10px' }}>
                      <span className={style.triggerDesc}>
                        <img src={imgUploadImg} />
                        <span className={style.triggerName}>发送图片</span>
                      </span>
                      <input
                        disabled={UserStatus > 0}
                        type="file"
                        accept=".png, .jpg, .jpeg"
                        onChange={e => {
                          this.uploadImage(e);
                        }}
                      />
                    </div> */}
                    {/* <div className={style.fileInput} style={{ marginLeft: '10px' }}>
                      <span className={style.triggerDesc}>
                        <img src={imgUploadFile} />
                        <span className={style.triggerName}>发送文件</span>
                      </span>
                      <input
                        disabled={UserStatus > 0}
                        type="file"
                        accept=".ppt, .pptx, .pdf, .doc, .docx, .xlsx, .xls"
                        onChange={e => {
                          this.uploadAffix(e);
                        }}
                      />
                    </div> */}
                    <Button className={UserStatus > 0? style.submitBtnDisabled:style.submitBtn} disabled={UserStatus > 0}   type="primary" onClick={this.handleSubmitMessage}>
                      {UserStatus > 0?"禁言中":"发送"}
                    </Button>
                  </div>
                </div>
          </Col>
          <Col flex="200px" className={style.video_right}>
                  <Collapse defaultActiveKey={['1']} style={{width:'200px',height: '728px', overflowY: 'auto',border: '1px' }}>
                    <Panel  key="1" header={
                      <Badge count={unReadKey.filter(key=> key != '0').length} offset={[10, 0]}>
                        <span>{`在线人数(${onlineCount}/${peopleCount})`}</span>
                      </Badge>}
                    >
                      {userInfoList &&
                        userInfoList.map((item, index) => {
                            return (
                              <div key={item.UserId} className={style.item} onDoubleClick={()=>this.add(item)}>
                                <img className={unReadKey.findIndex(key => key == item.UserId+"") > -1 ? classnames(style.img,style.blink) : style.img} src={item.UserFace ? item.UserFace : DiscordAvotor} />
                                {member(item)}
                              </div>
                            );
                        })}
                    </Panel>
                  </Collapse>
          </Col>
        </Row>
      </div>
    );
  }
}
