<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="培训班" required>
                <j-search-select-tag
                  ref="selectTag"
                  v-model="queryParam.classNo"
                  :triggerChange="true"
                  placeholder="请选择培训班"
                  dict="training_class,name,no"
                  :disabled="false"
                  @change="loadData()"
                />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>

                <!-- 打印按钮 -->
                <a-button
                  type="primary"
                  v-if="queryParam.classNo"
                  v-print="'#Schedule'"
                  icon="printer"
                  style="margin-left: 8px"
                  >打印</a-button
                >
              </span>
            </a-col>

            <template v-if="toggleSearchStatus"> </template>
          </a-row>
        </a-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="table-operator">
        <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
        <a-button type="primary" icon="download" @click="handleExportXls('课程表')">导出</a-button>
        <a-upload
          name="file"
          :showUploadList="false"
          :multiple="false"
          :headers="tokenHeader"
          :action="importExcelUrl"
          @change="handleImportExcel"
        >
          <a-button type="primary" icon="import">导入</a-button>
        </a-upload>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
        </a-dropdown> -->
      </div>

      <!-- table区域-begin -->
      <div v-if="queryParam.classNo" id="Schedule">
        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="simulation"
          :pagination="false"
          :loading="loading"
          class="j-table-force-nowrap"
          @change="handleTableChange"
        >
          <template slot="cell" slot-scope="text, record">
            <div @click="handleEdit(getCourseInfo(text, record.id))" class="j-cell" title="点击编辑">
              <span v-if="getCourseInfo(text, record.id).id"
                >{{ getCourseInfo(text, record.id).courseName }} /
                {{ getCourseInfo(text, record.id).teacher_dictText }}</span
              >
            </div>
          </template>
        </a-table>
      </div>

      <a-empty v-else description="请先选择一个培训班，再进行操作" />
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <trainingClassSchedule-modal ref="modalForm" @ok="modalFormOk"></trainingClassSchedule-modal>
    </a-card>
  </page-layout>
</template>

<script>
import Vue from 'vue'
import '@/assets/less/TableExpand.less'
import PageLayout from '@/components/page/PageLayout'
import TrainingClassScheduleModal from './modules/TrainingClassScheduleModal'
import { getAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'TrainingClassScheduleList',
  mixins: [JeecgListMixin],
  components: {
    PageLayout,
    TrainingClassScheduleModal,
  },
  data() {
    return {
      description: '课程表管理页面',
      title: '课程表',
      simulation: [{ time: '上午' }, {}, {}, {}, { time: '下午' }, {}, {}, {}, { time: '晚上' }, {}],
      // 表头
      columns: [
        {
          title: '时间',
          align: 'center',
          width: 30,
          dataIndex: 'time',
          customRender: (value, row, index) => {
            const obj = { children: value, attrs: {} }
            if (index % 4 === 0) {
              obj.attrs.rowSpan = 4
            }
            // These two are merged into above cell1
            if (index % 4 !== 0) {
              obj.attrs.rowSpan = 0
            }
            return obj
          },
        },
        {
          title: '节次',
          dataIndex: 'id',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '星期一',
          align: 'center',
          dataIndex: 'Mon',
          scopedSlots: { customRender: 'cell' },
        },
        {
          title: '星期二',
          align: 'center',
          dataIndex: 'Tue',
          scopedSlots: { customRender: 'cell' },
        },
        {
          title: '星期三',
          align: 'center',
          dataIndex: 'Wed',
          scopedSlots: { customRender: 'cell' },
        },
        {
          title: '星期四',
          align: 'center',
          dataIndex: 'Thur',
          scopedSlots: { customRender: 'cell' },
        },
        {
          title: '星期五',
          align: 'center',
          dataIndex: 'Fri',
          scopedSlots: { customRender: 'cell' },
        },
        {
          title: '星期六',
          align: 'center',
          dataIndex: 'Sat',
          scopedSlots: { customRender: 'cell' },
        },
        {
          title: '星期日',
          align: 'center',
          dataIndex: 'Sun',
          scopedSlots: { customRender: 'cell' },
        },
      ],
      url: {
        list: '/training/trainingClassSchedule/list',
      },

      /* 分页参数 */
      ipagination: {
        current: 1,
        pageSize: -1,
        pageSizeOptions: ['10', '20', '30'],
        showTotal: (total, range) => {
          return range[0] + '-' + range[1] + ' 共' + total + '条'
        },
        showQuickJumper: true,
        showSizeChanger: true,
        total: 0,
      },
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  created() {
    this.init()
    this.simulation = this.simulation.map((item, index) => {
      item.id = index + 1
      item.Mon = 'Mon'
      item.Tue = 'Tue'
      item.Wed = 'Wed'
      item.Thur = 'Thur'
      item.Fri = 'Fri'
      item.Sat = 'Sat'
      item.Sun = 'Sun'
      return item
    })
    this.loadData(1)
  },
  methods: {
    /**
     * 界面初始化时调用
     */
    init() {
      // 将用户习惯的查询取出
      Object.assign(this.queryParam, Vue.ls.get('USER_QUERY_CLASS_NO'))
    },
    /**
     * 加载数据
     * @param arg 参数
     */
    loadData(arg) {
      if (arg === 1) {
        this.ipagination.current = 1
      }
      var params = this.getQueryParams() //查询条件
      // 将查询条件存储到本地
      if (params.classNo) Vue.ls.set('USER_QUERY_CLASS_NO', { classNo: params.classNo })
      else return

      this.loading = true
      getAction(this.url.list, params)
        .then((res) => {
          if (res.success) {
            this.dataSource = res.result.records || res.result
            if (res.result.total) {
              this.ipagination.total = res.result.total
            } else {
              this.ipagination.total = 0
            }
          } else {
            this.$message.warning(res.message)
          }
        })
        .finally(() => {
          this.loading = false
        })
    },
    /**
     * 获取课程信息
     */
    getCourseInfo(day, several) {
      let a = this.dataSource.filter((x) => x.day === day && x.several === several)
      if (a.length > 0) {
        return a[0]
      } else {
        return { day, several, classNo: this.queryParam.classNo }
      }
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';

.j-cell {
  width: 100%;
  min-height: 16px;
}

.j-cell:hover {
  background-color: #f1f1f1;
  transition: 400ms;
}
</style>