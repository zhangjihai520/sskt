import React, { Component } from 'react';
import { Button, Row, Tabs } from 'antd';
import $ from 'jquery';
import style from './index.less';
import { connect } from 'dva';
import { getUrlQuery } from 'utils/utils';
import classnames from 'classnames';
import videoIcon from 'assets/common/videoIcon.png';

const Aliplayer = window.Aliplayer;
let player;
const { TabPane } = Tabs;

class MinLessonVideo extends Component {
	constructor(props) {
		super(props);
		this.state = {
			data: {},
			curIndex: 0,
		};
	}

	componentDidMount() {
		this.getSource();
	}

	getSource = () => {
		const WeiKeId = getUrlQuery(window.location.href, 'WeiKeId');
		this.props
			.dispatch({
				type: 'stuModel/GetWeiKeVideo',
				payload: {
					WeiKeId: WeiKeId
				}
			})
			.then(result => {
				this.setState(
					{
						data: result
					},
					() => {
						this.createPlayer(this.state.data.VideoUrl[0] ? this.state.data.VideoUrl[0] : this.state.data.PPTVideoUrl[0]);
					}
				);
			});
	};

	componentWillUnmount() {
		player = null;
	}

	createPlayer = source => {
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
				width: '750px',
				height: '420px',
				controlBarVisibility: 'click',
				source: source,
				vid: '',
				useH5Prism: true,
				useFlashPrism: false,
				playauth: '',
				cover: ''
			};
			player = new Aliplayer(option);
		});
	};

	handleSwitchRecord = (source, index) => {
		this.setState({ curIndex: index });
		this.createPlayer(source);
	};

	render() {
		const { data, curIndex } = this.state;
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
		return (
			<div>
				<div className={style.bg}>
					<div className={style.bgImg} />
					<div className={style.bgContent} />
				</div>
				<div className={style.contentArea}>
					<p className={style.videoName}>{data.WeiKeName}</p>
					<p className={style.videoDesc}>主讲教师：{data.CreateUserName}</p>
					<hr />
					<div className={style.videoPanel}>
						<div className="prism-player video-container" id="J_Player" style={{ float: 'left' }}></div>
						<div className={style.recordPanel}>
							<Tabs defaultActiveKey="1" className={style.tabs}>
								<TabPane tab="课程列表" key="1">
									<div className={style.detaiWrap}>
										{result &&
											result.map((item, index) => {
												let itemStyle = classnames(style.item, style.itemWithLine);
												if (curIndex == index) {
													itemStyle = classnames(style.item, style.itemWithLine, style.cur);
												}
												return (
													<div
														key={index}
														className={itemStyle}
														onClick={() => {
															this.handleSwitchRecord(item, index);
														}}
													>
														<span className={style.label}>课程</span>
														<div className={style.desc}>
															<div className={classnames(style.name, style.videoname)}>
																{data.WeiKeName}-{index + 1}
															</div>
														</div>
													</div>
												);
											})}
									</div>
								</TabPane>
							</Tabs>
						</div>
					</div>
				</div>
			</div>
		);
	}
}

export default connect()(MinLessonVideo);
