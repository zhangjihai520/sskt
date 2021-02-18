import React, { Component } from 'react';
import { Button, Row, Tabs } from 'antd';
import $ from 'jquery';
import style from './index.less';
import { connect } from 'dva';
import { getUrlQuery, flashChecker } from 'utils/utils';
import SwitchIcon from 'assets/common/switch_icon.png';
import classnames from 'classnames';
import Player from 'xgplayer'
import FlvJsPlayer from 'xgplayer-flv.js';
import HlsJsPlayer from 'xgplayer-hls.js';
const { TabPane } = Tabs;
//const Aliplayer = window.Aliplayer;
let player;

class RecordStream extends Component {
	constructor(props) {
		super(props);
		this.state = {
			data: {},
			firstReady: false,
			tarVideoStream: 2,
			curVideoIndex: 0
		};
	}

	componentDidMount() {
		this.getSource();
		// reset style
		$('.gd').css({ width: '100%' });
	}

	componentWillUnmount() {
		$('.gd').css({ width: '' });
		player = null;
	}

	getSource = () => {
		const SubjectId = getUrlQuery(window.location.href, 'SubjectId');
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
						data: result
					},
					() => {
						this.createPlayer(this.state.data.VideoUrl[0] || this.state.data.PPTVideoUrl[0]);
					}
				);
			});
	};

	/**createPlayer = source => {
		const self = this;
		$(function() {
			if (player) {
				player.dispose();
				$('#J_Player').empty();
				player = null;
			}

			let option = {
				id: 'J_Player',
				playerSwfPath: '/../PrismPlayer.swf',
				autoplay: false,
				isLive: false,
				width: '1010px',
				height: '570px',
				controlBarVisibility: 'click',
				source: source,
				vid: '',
				useH5Prism: true,
				useFlashPrism: false,
				playauth: '',
				cover: ''
			};
			player = new Aliplayer(option, function(e) {});
		});
	};**/
	createPlayer = (source) => {
		const self = this;
		if (player) {
		  player.destroy();
		  $('#player-con').empty();
		  player = null;
		}
		let option = {
		  id: 'player-con',
		  width: '1010px',
		  height: '570px',
		  autoplay: false,
		  videoInit: true,
		  lang: 'zh-cn',
		  //fluid: true,
		  fitVideoSize: 'auto',
		  loop: false,
		  url: source,
		  playsinline: true,
		  whitelist: [
				""
			]
		};
		$(function() {
		  if (source && source.indexOf('.mp4') > -1) {
			player =new Player(option);
		  }else if(source && source.indexOf('.m3u8') > -1)
		  {
			player = new HlsJsPlayer(option);
		  }else{
			player = new FlvJsPlayer(option);
		  }
		  if (option.autoplay) {
			player.reload();
			player.play();
		  }
		  self.setState({ firstReady: true });
		});
	  };

	handleSwitchRecord = index => {
		const { data, tarVideoStream } = this.state;

		let videoObject, backupVideoObject;
		this.setState({ curVideoIndex: index });
		if (tarVideoStream == 1) {
			videoObject = data.PPTVideoUrl[index];
			backupVideoObject = {
				videoUrl: data.VideoUrl[index],
				tarVideoStream: 2
			};
		} else {
			videoObject = data.VideoUrl[index];
			backupVideoObject = {
				videoUrl: data.PPTVideoUrl[index],
				tarVideoStream: 1
			};
		}

		if (videoObject) {
			this.createPlayer(videoObject);
		} else {
			this.createPlayer(backupVideoObject.videoUrl);
			this.setState({ tarVideoStream: backupVideoObject.tarVideoStream });
		}
	};

	handleSwitch = targetIndex => {
		const { curVideoIndex } = this.state;

		if (targetIndex == 1) {
			this.createPlayer(this.state.data.VideoUrl[curVideoIndex] || this.state.data.PPTVideoUrl[curVideoIndex]);
			this.setState({
				tarVideoStream: 2
			});
		} else {
			this.createPlayer(this.state.data.PPTVideoUrl[curVideoIndex] || this.state.data.VideoUrl[curVideoIndex]);
			this.setState({
				tarVideoStream: 1
			});
		}
	};

	render() {
		const { data, tarVideoStream, curVideoIndex } = this.state;
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
						<Row
							key={index}
							className={curVideoIndex == index ? classnames(style.recordListItem, style.cur) : style.recordListItem}
							onClick={() => {
								this.handleSwitchRecord(index);
							}}
						>
							<span>
								{data.SubjectName}-{index + 1}
							</span>
						</Row>
					);
				});
			}
		};

		return (
			<div className={style.contentArea}>
				<p className={style.videoName}>{data.SubjectName}</p>
				<p className={style.videoDesc}>主讲教师：{data.TeacherName}</p>
				<hr />
				<div className={style.videoPanel}>
				<div className="prism-player video-container" id="player-con" style={{ float: "left" }}></div>
					<div className={style.recordPanel}>
						<Tabs defaultActiveKey="1" className={style.tabs}>
							<TabPane tab="视频列表" key="1">
								{videoList()}
							</TabPane>
						</Tabs>
					</div>
				</div>
			</div>
		);
	}
}

export default connect()(RecordStream);
