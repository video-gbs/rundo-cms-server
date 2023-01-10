package com.runjian.device.constant;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Miracle
 * @date 2023/1/10 14:43
 */
@Slf4j
@Data
public class CircleArray<T> {

    /**
     * 安全锁
     */
    private ReentrantLock lock = new ReentrantLock();

    /**
     * 一轮次数
     */
    private Integer circleSize;

    /**
     * 当前指标所在位置，每隔一秒进入到下一个位置
     */
    private volatile long currentIndex = 0;

    /**
     * 数据所在节点
     */
    private Map<T, Long> dataIndex = new HashMap<>();

    /**
     * 实际数组
     */
    private Map<Long, Set<T>> timeMap = new HashMap<>();


    /**
     * 初始化循环数组
     * @param circleSize
     */
    public CircleArray(int circleSize){
        // 设定最小值为60s
        if (circleSize < 60){
            circleSize = 60;
        }
        this.circleSize = circleSize;
    }

    /**
     * 添加或修改数据
     * @param data 数据
     * @param offset 值
     */
    public void addOrUpdateTime(T data, Long offset){
        if (offset <= 0){
            throw new RuntimeException("非法偏移值");
        }
        try{
            lock.lock();
            Long oldPosition = dataIndex.get(data);
            long position = (currentIndex + offset) % circleSize;
            if (Objects.nonNull(oldPosition)){
                Set<T> dataSet = timeMap.get(oldPosition);
                if (dataSet.size() == 1){
                    timeMap.remove(oldPosition);
                }else {
                    dataSet.remove(data);
                }
            }
            dataIndex.put(data, position);
            Set<T> dataSet = timeMap.getOrDefault(position, new HashSet<>());
            dataSet.add(data);
            timeMap.put(position, dataSet);
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }

    }


    /**
     * 移除数据
     * @param data
     */
    public void delTime(T data){
        try{
            lock.lock();
            Long position = dataIndex.get(data);
            if (Objects.isNull(position)){
                return;
            }
            timeMap.remove(position);
            dataIndex.remove(data);
        }catch (Exception ex){
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    /**
     * 游标移动到下一个位置并获取数据
     * @return
     */
    public Set<T> next(){
        try{
            lock.lock();
            this.currentIndex = (this.currentIndex + 1) % circleSize;
            return timeMap.get(this.currentIndex);
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            lock.unlock();
            return null;
        }
    }
}
