<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24" :style="{ marginBottom: '24px' }">
      <user-info-widget></user-info-widget>
    </a-row>

    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="出勤人数" :total="cardCount.cq | NumberFormat">
          <a-tooltip title="近7天出勤人数变化趋势" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-area :dataSource="chartData.cq" x="日期" y="出勤人数" />
          </div>
          <template slot="footer"
            >今日出勤人数：<span>{{ todayCq }}</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="学习资料上传数" :total="cardCount.xxzl | NumberFormat">
          <a-tooltip title="近7天出勤学习资料上传数变化趋势" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-area :data-source="chartData.xxzl" x="日期" y="学习资料数" />
          </div>
          <template slot="footer"
            >今日上传数：<span>{{ todayXxzl }}</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="日志上传数" :total="cardCount.rzs | NumberFormat">
          <a-tooltip title="近7天日志上传数变化趋势" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-bar :dataSource="chartData.rzs" :height="50" />
          </div>
          <template slot="footer"
            >今日日志上传数：<span>{{ todayRzs }}</span></template
          >
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="6" :style="{ marginBottom: '24px' }">
        <chart-card :loading="loading" title="总访问量" :total="loginfo.totalVisitCount | NumberFormat">
          <a-tooltip title="最近一周访问量统计" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <div>
            <mini-bar :dataSource="visitInfo" :height="50" />
          </div>
          <template slot="footer"
            >今日访问：<span>{{ loginfo.todayVisitCount }}</span></template
          >
        </chart-card>
      </a-col>
    </a-row>

    <a-card :loading="loading" :bordered="false" :body-style="{ padding: '0' }">
     <class-schedule></class-schedule>
    </a-card>
  </div>
</template>

<script>
import ACol from 'ant-design-vue/es/grid/Col'
import ATooltip from 'ant-design-vue/es/tooltip/Tooltip'
import ChartCard from '@/components/ChartCard'
import MiniBar from '@/components/chart/MiniBar'
import MiniArea from '@/components/chart/MiniArea'
import IndexBar from '@/components/chart/IndexBar'
import BarMultid from '@/components/chart/BarMultid'
import DashChartDemo from '@/components/chart/DashChartDemo'
import UserInfoWidget from './UserInfoWidget'
import { getLoginfo, getVisitInfo } from '@/api/api'
import { getAction, deleteAction, putAction, postAction, httpAction } from '@/api/manage'
import ClassSchedule from '../schedule/TrainingClassScheduleList.vue'

export default {
  name: 'IndexBdc',
  components: {
    ATooltip,
    ACol,
    ChartCard,
    MiniArea,
    MiniBar,
    DashChartDemo,
    BarMultid,
    IndexBar,
    UserInfoWidget,
    ClassSchedule
  },
  data() {
    return {
      loading: true,
      cardCount: {
        cq: 0,
        xxzl: 0,
        rzs: 0,
      },

      todayCq: 0,
      todayXxzl: 0,
      todayRzs: 0,

      chartData: {
        cq: [],
        xxzl: [],
        rzs: [],
      },

      loginfo: {},
      visitInfo: [],
      url: {
        cq: '/sys/report/cq',
        xxzl: '/sys/report/xxzl',
        rzs: '/sys/report/rzs',
      },

      registerTypeList: [
        {
          text: '业务受理',
        },
        {
          text: '业务管理',
        },
        {
          text: '文件管理',
        },
        {
          text: '信息查询',
        },
      ],
    }
  },
  created() {
    this.initCq()
    this.initXxzl()
    this.initRzs()
    this.initLogInfo()
    setTimeout(() => {
      this.loading = false
    }, 200)
  },
  methods: {
    initCq() {
      const that = this
      getAction(that.url.cq, {}).then((res) => {
        that.chartData.cq = []
        res.data.forEach((a) => {
          that.chartData.cq.push({
            x: a.atd_date,
            y: a.num,
          })
        })
        that.todayCq = res.today
        that.cardCount.cq = res.total
      })
    },
    initXxzl() {
      const that = this
      getAction(that.url.xxzl, {}).then((res) => {
        that.chartData.xxzl = []
        res.data.forEach((a) => {
          that.chartData.xxzl.push({
            x: a.date,
            y: a.num,
          })
        })
        that.todayXxzl = res.today
        that.cardCount.xxzl = res.total
      })
    },
    initRzs() {
      const that = this
      getAction(that.url.rzs, {}).then((res) => {
        that.chartData.rzs = []
        res.data.forEach((a) => {
          that.chartData.rzs.push({
            x: a.date,
            y: a.num,
          })
        })
        that.todayRzs = res.today
        that.cardCount.rzs = res.total
      })
    },
    initLogInfo() {
      getLoginfo(null).then((res) => {
        if (res.success) {
          Object.keys(res.result).forEach((key) => {
            res.result[key] = res.result[key] + ''
          })
          this.loginfo = res.result
        }
      })
      getVisitInfo().then((res) => {
        if (res.success) {
          this.visitInfo = []
          res.result.forEach((a) => {
            this.visitInfo.push({
              x: a.type,
              y: a.visit,
            })
          })
        }
      })
    },
    goPage() {
      this.$message.success('根据业务自行处理跳转页面!')
    },
  },
}
</script>

<style lang="less" scoped>
/deep/ .page-header {
  display: none;
}
.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}

.item-group {
  padding: 20px 0 8px 24px;
  font-size: 0;
  a {
    color: rgba(0, 0, 0, 0.65);
    display: inline-block;
    font-size: 14px;
    margin-bottom: 13px;
    width: 25%;
  }
}

.item-group {
  .more-btn {
    margin-bottom: 13px;
    text-align: center;
  }
}

.list-content-item {
  color: rgba(0, 0, 0, 0.45);
  display: inline-block;
  vertical-align: middle;
  font-size: 14px;
  margin-left: 40px;
}

@media only screen and (min-width: 1600px) {
  .list-content-item {
    margin-left: 60px;
  }
}

@media only screen and (max-width: 1300px) {
  .list-content-item {
    margin-left: 20px;
  }
  .width-hidden4 {
    display: none;
  }
}
.list-content-item {
  span {
    line-height: 20px;
  }
}
.list-content-item {
  p {
    margin-top: 4px;
    margin-bottom: 0;
    line-height: 22px;
  }
}
.anty-list-cust {
  .ant-list-item-meta {
    flex: 0.3 !important;
  }
}
.anty-list-cust {
  .ant-list-item-content {
    flex: 1 !important;
    justify-content: flex-start !important;
    margin-left: 20px;
  }
}
</style>