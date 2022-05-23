<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="姓名">
              <j-input placeholder="请输入姓名" v-model="queryParam.username"></j-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button
                type="primary"
                @click="searchReset({ classNo: object.no })"
                icon="reload"
                style="margin-left: 8px"
                >重置</a-button
              >
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'" />
              </a>
            </span>
          </a-col>
          <template v-if="toggleSearchStatus"> </template>
        </a-row>
      </a-form>
    </div>

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus" :disabled="disabled">新增</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('培训班教师关系')">导出</a-button>
      <a-upload
        name="file"
        :showUploadList="false"
        :multiple="false"
        :headers="tokenHeader"
        :action="importExcelUrl"
        @change="handleImportExcel"
      >
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <a-dropdown v-if="selectedRowKeys.length > 0" :disabled="disabled">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete" />删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
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
        <span slot="username" slot-scope="text, record">
          <span
            >{{ text }}
            <a-tag color="blue" v-if="record.isHead == '1'" title="班主任">
              班主任
              <a-icon type="bulb" theme="twoTone" /> </a-tag
          ></span>
        </span>
        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)" :disabled="disabled">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown :disabled="disabled">
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
    <!-- table区域-end -->

    <!-- 表单区域 -->
    <trainingClassTeacher-modal ref="modalForm" @ok="modalFormOk"></trainingClassTeacher-modal>
    <select-user-modal ref="selectUserModal" @selectFinished="selectOK" :key="String(new Date())"></select-user-modal>
  </a-card>
</template>

<script>
import '@/assets/less/TableExpand.less'
import TrainingClassTeacherModal from './TrainingClassTeacherModal'
import SelectUserModal from './SelectUserModal.vue'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { putAction } from '@/api/manage'

export default {
  name: 'TrainingClassTeacherList',
  mixins: [JeecgListMixin],
  components: {
    TrainingClassTeacherModal,
    SelectUserModal,
  },
  props: {
    // 1. 对象数据
    object: Object,
    // 通用禁用标识
    disabled: {
      type: Boolean,
      default: true,
      required: false,
    },
  },
  data() {
    return {
      description: '培训班教师关系管理页面',
      queryParam: {
        classNo: this.object.no,
      },
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
          title: '姓名',
          align: 'center',
          dataIndex: 'username_dictText',
          scopedSlots: { customRender: 'username' },
        },
        {
          title: '创建时间',
          align: 'center',
          dataIndex: 'createTime',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/training/trainingClassTeacher/list',
        link: '/training/trainingClassTeacher/link',
        delete: '/training/trainingClassTeacher/delete',
        deleteBatch: '/training/trainingClassTeacher/deleteBatch',
        exportXlsUrl: 'training/trainingClassTeacher/exportXls',
        importExcelUrl: 'training/trainingClassTeacher/importExcel',
      },
    }
  },
  computed: {
    importExcelUrl: function () {
      return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`
    },
  },
  methods: {
    handleAdd() {
      this.$refs.selectUserModal.queryParam = {
        classNo: this.object.no,
        type: 'teacher',
      }
      this.$refs.selectUserModal.visible = true
      this.$refs.selectUserModal.loadData()
    },
    selectOK(data) {
      let params = {}
      params.classNo = this.object.no
      params.usernameList = []
      for (var a = 0; a < data.length; a++) {
        params.usernameList.push(data[a])
      }
      putAction(this.url.link, params).then((res) => {
        if (res.success) {
          this.$message.success(res.message)
          this.loadData()
        } else {
          this.$message.warning(res.message)
        }
      })
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>