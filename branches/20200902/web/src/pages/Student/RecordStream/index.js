import React, { Component } from 'react';
import { Button, Row } from 'antd';
import $ from 'jquery';
import style from './index.less';
import { connect } from 'dva';
import { getUrlQuery } from 'utils/utils';
import Player from 'xgplayer'
import FlvJsPlayer from 'xgplayer-flv.js';
import HlsJsPlayer from 'xgplayer-hls.js';
//const Aliplayer = window.Aliplayer;
let player;

class RecordStream extends Component {
	constructor(props) {
		super(props);
		this.state = {
			firstReady: false,
			data: {}
		}
	}

	componentDidMount() {
		this.getSource();
		// reset style
		$(".gd").css({ width: '100%' });
	}

	componentWillUnmount() {
		$(".gd").css({ width: '' });
		player = null;
	}

	getSource = () => {
		const SubjectId = getUrlQuery(window.location.href, 'SubjectId');
		this.props.dispatch({
			type: 'stuModel/GetSubjectVideo',
			payload: {
				SubjectId: SubjectId
			}
		}).then(result => {
			this.setState({
				data: result,
			}, () => {
				this.createPlayer(this.state.data.VideoUrl[0] ? this.state.data.VideoUrl[0] : this.state.data.PPTVideoUrl[0]);
			});
		});
	};

	/**createPlayer = (source) => {
		const self = this;
		$(function(){
			if (player) {
				player.dispose();
				$('#player-con').empty();
				player = null;
			}

			let option = {
				id: "player-con",
				playerSwfPath: "/../PrismPlayer.swf",
				autoplay: false,
				isLive: false,
				width: "1280px",
				height: "720px",
				controlBarVisibility: "click",
				source: source,
				vid: "",
				useH5Prism:true,
				useFlashPrism:false,
				playauth: "",
				cover: "",
			}
			player = new Aliplayer(option, function(e) {
				self.setState({ firstReady: true });
			});
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

	handleSwitchRecord = (source) => {
		this.setState({ firstReady: false });
		this.createPlayer(source);
	};

	render() {
		const { data } = this.state;
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
				<hr/>
				<div className={style.videoPanel}>
					<div className="prism-player video-container" id="player-con" style={{ float: "left" }}></div>
					<div className={style.recordPanel}>
					 	{videoList()}
					</div>
				</div>
			</div>
		);
	}
}

export default connect()(RecordStream);