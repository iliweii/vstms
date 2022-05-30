<template>
  <a-drawer :title="title" width="70%" placement="right" :closable="false" @close="close" :visible="visible">
    <a-card :bordered="false">
      <!-- 操作按钮区域 -->
      <div class="table-operator" style="height: 40px">
        <a-button
          type="primary"
          icon="download"
          @click="handleExportTemp(queryParam.courseName + '成绩表-' + getNowFormatDate())"
          >导出成绩表</a-button
        >
      </div>

      <!-- table区域-begin -->
      <div>
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
          @change="handleTableChange"
        >
          <span slot="action" slot-scope="text, record">
            <a @click="handleEdit(record)">编辑</a>
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <trainingScore-modal ref="modalForm" @ok="modalFormOk"></trainingScore-modal>
    </a-card>
    <div class="drawer-bootom-button">
      <a-button type="primary" @click="handleCancel">确定</a-button>
      <a-button type="primary" @click="handleCancel">取消</a-button>
    </div>
  </a-drawer>
</template>

<script>
import '@/assets/less/TableExpand.less'
import TrainingScoreModal from './TrainingScoreModal'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { deleteAction, getAction, downFile, getFileAccessHttpUrl } from '@/api/manage'

export default {
  name: 'TrainingScoreList',
  mixins: [JeecgListMixin],
  components: {
    TrainingScoreModal,
  },
  data() {
    return {
      description: '成绩管理管理页面',
      visible: false,
      title: '操作',
      model: {},
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
          title: '课程名称',
          align: 'center',
          dataIndex: 'courseName',
        },
        {
          title: '学员姓名',
          align: 'center',
          dataIndex: 'username_dictText',
        },
        {
          title: '成绩',
          align: 'center',
          dataIndex: 'score',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/training/trainingScore/list',
        exportTemplate: 'training/trainingScore/exportTemplate',
      },
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    add() {
      //初始化默认值
      this.edit({})
    },
    edit(record) {
      this.model = Object.assign({}, record)
      this.visible = true
      this.queryParam = {
        classNo: record.classNo,
        courseName: record.courseName,
      }
      this.loadData()
    },
    close() {
      this.$emit('close')
      this.visible = false
      this.searchReset()
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
    /**
     * 获取当前日期
     */
    getNowFormatDate() {
      var date = new Date()
      var seperator1 = '-'
      var month = date.getMonth() + 1
      var strDate = date.getDate()
      if (month >= 1 && month <= 9) {
        month = '0' + month
      }
      if (strDate >= 0 && strDate <= 9) {
        strDate = '0' + strDate
      }
      var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
      return currentdate
    },
    handleCancel() {
      this.close()
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
/**Button按钮间距*/
.ant-btn {
  margin-left: 30px;
  margin-bottom: 30px;
  float: right;
}
/**抽屉按钮样式*/
.drawer-bootom-button {
  position: absolute;
  bottom: -8px;
  width: 100%;
  border-top: 1px solid #e8e8e8;
  padding: 10px 16px;
  text-align: right;
  left: 0;
  background: #fff;
  border-radius: 0 0 2px 2px;
}
</style>