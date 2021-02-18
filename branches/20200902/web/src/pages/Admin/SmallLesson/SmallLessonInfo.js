import React, { Component } from 'react';
import { Tabs } from 'antd';
import classnames from 'classnames';
import $ from 'jquery';
import style from './SmallLessonInfo.less';
import { connect } from 'dva';
import imgPoint from 'assets/common/point.png';
import videoIcon from 'assets/common/videoIcon.png';

const { TabPane } = Tabs;
const Aliplayer = window.Aliplayer;
let player;

@connect(({ commonModel }) => ({
	GradeList: commonModel.GradeList,
	CourseList: commonModel.CourseList
}))
class SmallLessonInfo extends Component {
	constructor(props) {
		super(props);
		this.state = {
			WeikeData: {}
		};
	}

	componentDidMount() {
		this.getSource();
	}

	componentWillUnmount() {
		player = null;
	}

	getSource = () => {
		const { dispatch, match } = this.props;
		const WeiKeId = match.params.WeiKeId;

		dispatch({
			type: 'smallnModel/GetWeiKeInfo',
			payload: {
				WeiKeId: WeiKeId
			}
		}).then(result => {
			console.log(result);
			this.setState(
				{
					WeikeData: result
				},
				() => {
					this.createPlayer(result.VideoUrl[0].Url);
				}
			);
		});
	};

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
			player = new Aliplayer(option);
		});
	};

	handleSwitchVideo = Url => {
		this.createPlayer(Url);
	};

	render() {
		const { WeikeData } = this.state;
		const { GradeList, CourseList } = this.props;

		return (
			<div className={style.contentArea}>
				<div className={style.videoName}>{WeikeData.WeiKeName}</div>
				<div className={style.videoDesc}>主讲教师：{WeikeData.CreateUserName}</div>
				<div className={style.line} />
				<div className={style.contentWrap}>
					<div className="prism-player video-container" id="J_Player" style={{ float: 'left' }}></div>
					<div className={style.recordPanel}>
						<Tabs defaultActiveKey="1" className={style.tabs}>
							<TabPane tab="微课详情" key="1">
								<div className={style.detaiWrap}>
									<div className={style.item}>
										<img className={style.img} src={imgPoint} />
										<div className={style.desc}>
											<div className={style.name}>课堂名称</div>
											<div>{WeikeData.WeiKeName}</div>
										</div>
									</div>
									<div className={style.item}>
										<img className={style.img} src={imgPoint} />
										<div className={style.desc}>
											<div className={style.name}>老师</div>
											<div>{WeikeData.CreateUserName}</div>
										</div>
									</div>
									<div className={style.item}>
										<img className={style.img} src={imgPoint} />
										<div className={style.desc}>
											<div className={style.name}>年级</div>
											<div>
												{WeikeData.GradeId &&
													GradeList.find(item => {
														return item.Id == WeikeData.GradeId;
													}).Name}
											</div>
										</div>
									</div>
									<div className={style.item}>
										<img className={style.img} src={imgPoint} />
										<div className={style.desc}>
											<div className={style.name}>学段学科</div>
											<div>
												{WeikeData.CourseId &&
													CourseList.find(item => {
														return item.Id == WeikeData.CourseId;
													}).LongName}
											</div>
										</div>
									</div>
									<div className={style.item}>
										<img className={style.img} src={imgPoint} />
										<div className={style.desc}>
											<div className={style.name}>章节</div>
											<div>{WeikeData.SectionName}</div>
										</div>
									</div>
								</div>
							</TabPane>
							<TabPane tab="课程列表" key="2">
								<div className={style.detaiWrap}>
									{WeikeData.VideoUrl &&
										WeikeData.VideoUrl.map((item, index) => {
											return (
												<div
													key={index}
													className={classnames(style.item, style.itemWithLine)}
													onClick={() => {
														this.handleSwitchVideo(item.Url);
													}}
												>
													<img className={style.img} src={videoIcon} />
													<div className={style.desc}>
														<div className={classnames(style.name, style.videoname)}>
															{WeikeData.WeiKeName}-{index + 1}
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
		);
	}
}

export default connect()(SmallLessonInfo);
