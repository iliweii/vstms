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
                />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="评选状态">
                <j-search-select-tag
                  v-model="queryParam.status"
                  :triggerChange="true"
                  placeholder="请选择培训状态"
                  dict="graduation_class_select_status"
                  :disabled="false"
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
          <span slot="action" slot-scope="text, record">
            <!-- 未确认 -->
            <template v-if="record.status == '0'">
              <a @click="handleConfirm(record)">确认评选</a>
            </template>

            <!-- 已完成 -->
            <template v-else-if="record.status == '4'">
              <a @click="handleDetail(record)">详情</a>
            </template>

            <!-- 进行中 -->
            <template v-else>
              <a @click="handleEdit(record)">编辑</a>

              <a-divider v-if="record.status == '1'" type="vertical" />
              <a-dropdown v-if="record.status == '1'">
                <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
                <a-menu slot="overlay">
                  <a-menu-item>
                    <a-popconfirm title="确定取消确认评选优秀学员吗?" @confirm="() => handleCancelConfirm(record)">
                      <a>取消确认</a>
                    </a-popconfirm>
                  </a-menu-item>
                </a-menu>
              </a-dropdown>
            </template>
          </span>
        </a-table>
      </div>
      <!-- table区域-end -->

      <!-- 表单区域 -->
      <graduationSelectClass-modal ref="modalForm" @ok="modalFormOk"></graduationSelectClass-modal>
    </a-card>
  </page-layout>
</template>

<script>
import '@/assets/less/TableExpand.less'
import PageLayout from '@/components/page/PageLayout'
import GraduationSelectClassModal from './modules/SelectClassBaseModal'
import { putAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'

export default {
  name: 'GraduationSelectClassList',
  mixins: [JeecgListMixin],
  components: {
    PageLayout,
    GraduationSelectClassModal,
  },
  data() {
    return {
      description: '评选优秀学员管理页面',
      title: '评选优秀学员',
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
          title: '培训班名称',
          align: 'center',
          dataIndex: 'classNo_dictText',
        },
        {
          title: '评选状态',
          align: 'center',
          dataIndex: 'status_dictText',
        },
        {
          title: '操作',
          dataIndex: 'action',
          align: 'center',
          scopedSlots: { customRender: 'action' },
        },
      ],
      url: {
        list: '/graduation/graduationSelectClass/list',
        edit: '/graduation/graduationSelectClass/edit',
      },
    }
  },
  methods: {
    /**
     * 确认评选
     */
    handleConfirm(param) {
      const that = this
      param.status = '1'
      putAction(that.url.edit, param).then((res) => {
        that.$message.success('确认成功')
        that.loadData()
      })
    },
    /**
     * 取消确认评选
     */
    handleCancelConfirm(param) {
      const that = this
      param.status = '0'
      putAction(that.url.edit, param).then((res) => {
        that.$message.success('取消确认成功')
        that.loadData()
      })
    },
  },
}
</script>
<style scoped>
@import '~@assets/less/common.less';
</style>