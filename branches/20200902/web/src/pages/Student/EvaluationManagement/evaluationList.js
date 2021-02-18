import { Button, Rate, Empty } from 'antd';
import styles from './index.less';
import avator from 'assets/common/discord_avotor.png';
import alreadyComment from 'assets/common/already_comment.png';

export default function EvaluationList(props) {
  const { dataList = [], onClick } = props;
  const statusArr = ['待上架', '待报名', '报名中', '待上课', '已结课', '上课中'];
  const starArr = ['一星', '二星', '三星', '四星', '五星'];
  return (
    <ul className={styles.container}>
      {dataList.length > 0 ? (
        dataList.map((item, i) => {
          return (
            <li key={i} className={styles.listWrap}>
              <div className={styles.listItem}>
                <div className={styles.cover}>
                  <img
                    src={item.ImagePath ? item.ImagePath : require(`assets/common/courseImg/${item.ShortCode}.png`)}
                    alt=""
                  />
                </div>
                <div className={styles.info}>
                  <h5>{item.SubjectName}</h5>
                  <p>{item.CourseName}</p>
                  <p>{item.TeacherName}</p>
                  <p>课程时间：{item.BeginTime}</p>
                  <p>{statusArr[item.StatusFlag - 1]}</p>
                  <div className={styles.content}>
                    <span>评星：</span>
                    <div>
                      <Rate value={item.EvaluateLevel} disabled defaultValue={item.EvaluateLevel} style={{ fontSize: 14 }} />
                    </div>
                  </div>
                </div>
                <div className={styles.operation}>
                  {item.IsEvaluate === 0 ? (
                    <Button
                      type="primary"
                      onClick={() => onClick && onClick(item.SubjectId, item.SubjectRoomId, item.TeacherId)}
                    >
                      评价
                    </Button>
                  ) : (
                    <img src={alreadyComment} />
                  )}
                </div>
              </div>
              {item.IsEvaluate === 0 ? (
                ''
              ) : (
                <div className={styles.commentWrap}>
                  <img className={styles.avator} src={item.TeacherFace ? item.TeacherFace : avator} />
                  <div className={styles.rightDescWrap}>
                    <div>{item.EvaluateComment}</div>
                  </div>
                </div>
              )}
            </li>
          );
        })
      ) : (
        <div className={styles.noData}>
          <Empty />
        </div>
      )}
    </ul>
  );
}
