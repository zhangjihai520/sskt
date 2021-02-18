import React, { Component } from 'react';
import { Button, Input, message, Tabs, Popover, Collapse, Row, Col, Badge} from 'antd';
import $ from 'jquery';
import style from './index.less';
import { connect } from 'dva';
import { getUrlQuery } from 'utils/utils';
import imgExcel from 'assets/common/img_excel.png';
import imgPdf from 'assets/common/img_pdf.png';
import imgPPT from 'assets/common/img_ppt.png';
import imgWord from 'assets/common/img_word.png';
import DiscordAvotor from 'assets/common/discord_avotor.png';
import { colorArr, emojiArr } from 'config/enum';
import classnames from 'classnames';
import _ from 'lodash';
import Zmage from 'react-zmage'
import Player from 'xgplayer'
import FlvJsPlayer from 'xgplayer-flv.js';
import HlsJsPlayer from 'xgplayer-hls.js';
const { TextArea } = Input;
const { TabPane } = Tabs;
const { Panel } = Collapse;
let player;
let ws;
let timeoutObj;
const initialPanes = [{title: '互动区', key: '0', messages: [], closable: false, scrollActive: true}];
@connect(({ commonModel }) => ({
  studentBasePath: commonModel.StudentBasePath,
  CurUserSystem: commonModel.CurUserSystem
}))
export default class RecordStream extends Component {
  constructor(props) {
    super(props);
    this.state = {
		UserId: 0,
		message: '',
		data: {},
		activeKey: initialPanes[0].key,
		panes: initialPanes,
		loading: false,
		userInfoList: [],
		playerReady: false,
		beforeScrollHight:0
    };
  }
  componentDidMount() {
    this.getSource();
    $('.gd').css({ width: '100%' });
  }

  componentWillUnmount() {
    $('.gd').css({ width: '' });
    if(player)
    {
      player.destroy();
      player=null;
    }
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
        this.setState(
        {
			data: result,
			UserId: result.Uid
        },() => {
			this.createPlayer(this.state.data.VideoUrl[0] ? this.state.data.VideoUrl[0] : this.state.data.PPTVideoUrl[0]);
			this.getMessage(0);
        });        
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
      width: '555px',
      height: '312px',
      autoplay: true,
      videoInit: true,
      lang: 'zh-cn',
      volume: 1,
      fitVideoSize: 'auto',
      loop: false,
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
    	player.once('ready',()=>{
    		self.setState({
          		playerReady: true
        	});
      	});
    	if (option.autoplay) {
			player.reload();
      	}
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
    });
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
	  activeKey: newActiveKey
	});
  };
  handleSwitchRecord = (source) => {
	this.createPlayer(source);
  };
  render() {
    const { data, userInfoList, panes, activeKey} = this.state;

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
        <div className={classnames(style.name, style.off)}>
          {item.UserName}
        </div>
      );
    };

    const tabTitle = pane =>{
      return (
          <span>{pane.title}</span>
      )
    }
	let result = [];
	if (data.VideoUrl) {
		data.VideoUrl.map((item) => {
			result.push(item)
		})
	}
	if (data.PPTVideoUrl) {
		data.PPTVideoUrl.map((item) => {
			result.push(item)
		})
	}
	const videoList = () => {
		if (result) {
			return result.map((item, index) => {
				return (
					<Row key={index} className={style.recordListItem} onClick={() => {this.handleSwitchRecord(item)}}>
						 <span>{data.SubjectName}-{index+1}</span>
					 </Row>
				)
			});
		}
	}
    return (
		<div className={style.contentArea}>
		  <p className={style.videoName}>{data.SubjectName}</p>
		  <p className={style.videoDesc}>主讲教师：{data.TeacherName}</p>
		  <hr />
		  <Row className={style.videoPanel}>
			<Col flex="646px" className={style.video_leftPanel} >
			  <div
				className={style.top}
				id="player-con"
			  ></div>
			  <div className={style.bottom}>
			  <Tabs defaultActiveKey="1" className={style.tabs}>
				  <TabPane tab="视频列表" key="1">
					  {videoList()}
				  </TabPane>
			  </Tabs>
			  </div>
			</Col>
  
			<Col flex="1" className={style.video_center}>
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
			</Col>
			<Col flex="200px" className={style.video_right}>
					<Collapse defaultActiveKey={['1']} style={{minWidth:'150px',height: '699px', overflowY: 'auto' }}>
					  <Panel key="1" header={
						  <span>用户列表</span>}
					  >
						{userInfoList &&
						  userInfoList.map((item, index) => {
							  return (
								<div key={item.UserId} className={style.item} onDoubleClick={()=>this.add(item)}>
								  <img className={style.img} src={item.UserFace ? item.UserFace : DiscordAvotor} />
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
