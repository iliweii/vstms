<template>
  <page-layout :title="title">
    <a-card :bordered="false">
      <!-- 查询区域 -->
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="searchQuery">
          <a-row :gutter="24">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="培训班">
                <j-search-select-tag
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
              <a-form-item label="考勤日期">
                <j-date
                  placeholder="请选择考勤日期"
                  style="width: 100%"
                  v-model="queryParam.atdDate"
                  :showTime="true"
                  dateFormat="YYYY-MM-DD"
                  @change="loadData()"
                />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="考勤状态">
                <j-search-select-tag
                  v-model="queryParam.atdStatus"
                  :triggerChange="true"
                  placeholder="请选择考勤状态"
                  dict="training_attendance_status"
                  :disabled="false"
                  @change="loadData()"
                />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
                <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
                <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
                <a @click="handleToggleSearch" style="margin-left: 8px">
                  {{ toggleSearchStatus ? '收起' : '展开' }}
                  <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
                </a>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <!-- 操作按钮区域 -->
      <div class="table-operator" v-if="queryParam.classNo">
        <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button> -->
        <!-- <a-button type="primary" icon="download" @click="handleExportXls('考勤管理')">导出</a-button> -->
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
        <a-button type="primary" icon="download" @click="handleExportTemp('考勤表')">导出考勤表模板</a-button>
        <a-button type="primary" icon="download" @click="handleExportError('考勤表导入错误信息')"
          >导出错误信息</a-button
        >
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
        </a-dropdown>

        <a-alert
          v-if="!uploadStatus"
          :description="
            queryParam.atdDate
              ? `${queryParam.atdDate}日考勤表未上传`
              : '今日还未上传本培训班的考勤表，请及时下载考勤模板，填写考勤情况，并上传考勤表。'
          "
          type="error"
          closable
        />
        <a-alert
          v-else
          :message="`${
            queryParam.atdDate ? queryParam.atdDate + '日' : '今日'
          }考勤已上传，本培训班共 ${totalNum}人，出勤 ${attendanceNum}人，出勤率 ${(
            (attendanceNum / totalNum) *
            100
          ).toFixed(0)}%。`"
          type="success"
          show-icon
        />
      </div>

      <!-- table区域-begin -->
      <div v-if="queryParam.classNo">
        <div class="ant-alert ant-alert-info" style="margin-bottom: 16px">
          <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择
          <a style="font-weight: 600">{{ selectedRowKeys.length }}</a
          >项
          <a style="margin-left: 24px" @click="onClearSelected">清空</a>
        </div>

        <a-table
          ref="table"
          size="middle"
          bordered
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="ipagination"
          :loading="loading"
          class="j-table-force-nowrap"
          :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
          @change="handleTableChange"
        >
          <span slot="atdStatus" slot-scope="text, record">
            <a-tag :color="['green', 'pink', 'red', 'orange'][record.atdStatus]"> {{ text }} </a-tag>
          </span>
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>

            <a-divider type="vertical" />
            <a-dropdown>
              <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
              <a-menu slot="overlay">
                <a-menu-item>
                  <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                    <a>删除</a>
                  </a-popconfirm>
                </a-menu-item>
              </a-menu>
            </a-dropdown>
          </span>
        </a-table>
      </div>
      <a-empty v-else description="请先选择一个培训班，再进行操作" />
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <trainingAttendance-modal ref="modalForm" @ok="modalFormOk"></trainingAttendance-modal>
    </a-card>
  </page-layout>
</template>

<script>
import Vue from 'vue'
import '@/assets/less/TableExpand.less'
import PageLayout from '@/components/page/PageLayout'
import TrainingAttendanceModal from './modules/TrainingAttendanceModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { deleteAction, getAction, downFile, getFileAccessHttpUrl } from '@/api/manage'

export default {
  name: 'TrainingAttendanceList',
  mixins: [JeecgListMixin],
  components: {
    PageLayout,
    TrainingAttendanceModal,
  },
  data() {
    return {
      description: '考勤管理管理页面',
      title: '考勤管理',
      uploadStatus: false, // 今日上传状态
      totalNum: 0, // 培训班总人数
      attendanceNum: 0, // 已考勤人数
      // 表头
      columns: [
        {
          title: '#',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '培训班编号',
          align: 'center',
          dataIndex: 'classNo',
        },
        {
          title: '学员用户名',
          align: 'center',
          dataIndex: 'username',
        },
        {
          title: '学员姓名',
          align: 'center',
          dataIndex: 'username_dictText',
          sorter: (a, b) => a.username - b.username,
        },
        {
          title: '考勤日期',
          align: 'center',
          dataIndex: 'atdDate',
          sorter: (a, b) => a.atdDate - b.atdDate,
        },
        {
          title: '考勤状态',
          align: 'center',
          dataIndex: 'atdStatus_dictText',
          sorter: (a, b) => a.atdStatus - b.atdStatus,
          scopedSlots: { customRender: 'atdStatus' },
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/training/trainingAttendance/list',
        delete: '/training/trainingAttendance/delete',
        deleteBatch: '/training/trainingAttendance/deleteBatch',
        exportXlsUrl: 'training/trainingAttendance/exportXls',
        errorXls: 'training/trainingAttendance/errorXls',
        exportTemplate: 'training/trainingAttendance/exportTemplate',
        importExcelUrl: 'training/trainingAttendance/importExcel',
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
    this.loadData()
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
            if (res.message == 'false') this.uploadStatus = false
            else {
              let a = res.message.split('|')
              this.uploadStatus = true
              this.totalNum = a[1]
              this.attendanceNum = a[2]
            }
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
    handleExportTemp(fileName) {
      let param = this.getQueryParams()
      console.log('导出参数', param)
      downFile(this.url.exportTemplate, param).then((data) => {
        if (!data) {
          this.$message.warning('文件下载失败')
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data], { type: 'application/vnd.ms-excel' }), fileName + '.xls')
        } else {
          let url = window.URL.createObjectURL(new Blob([data], { type: 'application/vnd.ms-excel' }))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', fileName + '.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link) //下载完成移除元素
          window.URL.revokeObjectURL(url) //释放掉blob对象
        }
      })
    },
    handleExportError(fileName) {
      let param = this.getQueryParams()
      console.log('导出参数', param)
      downFile(this.url.errorXls, param).then((data) => {
        if (!data) {
          this.$message.warning('文件下载失败')
          return
        } else if (data.type === 'application/json') {
          this.$message.warning('暂无错误信息')
          return
        }
        if (typeof window.navigator.msSaveBlob !== 'undefined') {
          window.navigator.msSaveBlob(new Blob([data], { type: 'application/vnd.ms-excel' }), fileName + '.xls')
        } else {
          let url = window.URL.createObjectURL(new Blob([data], { type: 'application/vnd.ms-excel' }))
          let link = document.createElement('a')
          link.style.display = 'none'
          link.href = url
          link.setAttribute('download', fileName + '.xls')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link) //下载完成移除元素
          window.URL.revokeObjectURL(url) //释放掉blob对象
        }
      })
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>