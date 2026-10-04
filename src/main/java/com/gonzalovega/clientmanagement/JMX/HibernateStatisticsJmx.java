package com.gonzalovega.clientmanagement.JMX;

import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;
import org.hibernate.stat.Statistics;


@Component
@ManagedResource(
        objectName = "com.gonzalovega.clientmanagement:type=Hibernate,name=Statistics",
        description= "Hibernate ORM runtime statistics"
)
public class HibernateStatisticsJmx {

    private final Statistics stadistics;



    public HibernateStatisticsJmx(EntityManagerFactory emfVT) {
        this.stadistics = emfVT.unwrap(SessionFactory.class).getStatistics();
    }

    /**
     * For the sessions
     *
     * getSessionOpenCount() = Total sessions opened
     * getSessionCloseCount() = Total sessions closed
     *
     */
    @ManagedAttribute(description = "Number od sessions opened")
    public long getSessionOpenCount() {
        return stadistics.getSessionOpenCount();
    }

    @ManagedAttribute(description = "Number of sessions closed")
    public long getSessionCloseCount() {
        return stadistics.getSessionCloseCount();
    }



    /**
     * For the connections
     *
     * getConnectCount() = Total DB connections obtained
     * getConnectCount() = average time query
     */
    @ManagedAttribute(description = "Number od database connectins obtained")
    public long getConnectCount(){
        return stadistics.getConnectCount();
    }

    @ManagedAttribute(description = "Average query execution time in ms")
    public double getQueryExecutionAvgTime() {
        long count = stadistics.getQueryExecutionCount();
        if (count == 0) return 0;
        return (double) stadistics.getQueryExecutionMaxTime() / count;
    }

    /**
     * For the Transactions
     *
     * getTransactionsCount() = Total transactions completed
     * getSuccessfulTransactionCount() = Total successful transactions
     * getQueryExecutionAvgTime() = Average query execution time (ms)
     *
     */
    @ManagedAttribute(description = "number of transactions completed")
    public long getTransactionsCount() {
        return stadistics.getTransactionCount();
    }

    @ManagedAttribute(description = "Number of successful transactions")
    public long getSuccessfulTransactionCount() {
        return stadistics.getSuccessfulTransactionCount();
    }

    /**
     *Queries
     *
     * getQueryExecutionCount() = Total queries executed
     * getQueryExecutionMaxTime() = Slowest query time (ms)
     * getQueryExecutionMaxTimeQueryString() = SQL of the slowest query
     * getQueryCacheHitCount() = Query cache hits (cache worked)
     * getQueryCacheMissCount() = Query cache misses (cache not used)
     * getQueryCachePutCount() = Queries stored into cache
     *
     */
    @ManagedAttribute(description = "Number of queries executed")
    public long getQueryExecutionCount() {
        return stadistics.getQueryExecutionCount();
    }

    @ManagedAttribute(description = "Maximum query execution time in milliseconds")
    public long getQueryExecutionMaxTime() {
        return stadistics.getQueryExecutionMaxTime();
    }

    @ManagedAttribute(description = "HQL/SQL of the slowest query recorded")
    public String getQueryExecutionMaxTimeQueryString() {
        return stadistics.getQueryExecutionMaxTimeQueryString();
    }

    // To check if the cache is being used correctly.
    // If there are many misses and few hits, the cache is not working properly.
    @ManagedAttribute(description = "Query cache hit count")
    public long getQueryCacheHitCount() {
        return stadistics.getQueryCacheHitCount();
    }

    @ManagedAttribute(description = "Query cache miss count")
    public long getQueryCacheMissCount() {
        return stadistics.getQueryCacheMissCount();
    }

    @ManagedAttribute(description = "Query cache put count")
    public long getQueryCachePutCount() {
        return stadistics.getQueryCachePutCount();
    }

    /**
     * Entities
     *
     * getEntityLoadCount() = Total entities loaded
     * getEntityInsertCount() = Total entities inserted
     * getEntityUpdateCount() = Total entities updated
     * getEntityDeleteCount() = Total entities deleted
     * getEntityFetchCount() = Total entity fetch operations
     *
     */
    @ManagedAttribute(description = "Number of entity loads")
    public long getEntityLoadCount() {
        return stadistics.getEntityLoadCount();
    }

    @ManagedAttribute(description = "Number of entity inserts")
    public long getEntityInsertCount() {
        return stadistics.getEntityInsertCount();
    }

    @ManagedAttribute(description = "Number of entity updates")
    public long getEntityUpdateCount() {
        return stadistics.getEntityUpdateCount();
    }

    @ManagedAttribute(description = "Number of entity deletes")
    public long getEntityDeleteCount() {
        return stadistics.getEntityDeleteCount();
    }

    @ManagedAttribute(description = "Number of entity fetch operations")
    public long getEntityFetchCount() {
        return stadistics.getEntityFetchCount();
    }

    /**
     * Collections
     *
     * getCollectionLoadCount() = Total collections loaded
     * getCollectionFetchCount() = Total collections fetched — if higher than EntityLoadCount, N+1 problem
     *
     */

    // if getcollectionloadcount() is bigger than getCollectionFetchCount we have a n+1 problem
    // (Hibernate are throwing too much queries)
    @ManagedAttribute(description = "Number of collections loaded")
    public long getCollectionLoadCount() {
        return stadistics.getCollectionLoadCount();
    }

    @ManagedAttribute(description = "Number of collections fetched")
    public long getCollectionFetchCount() {
        return stadistics.getCollectionFetchCount();
    }

    /**
     * Second-level cache
     *
     * getSecondLevelCacheHitCount() = L2 cache hits
     * getSecondLevelCacheMissCount() = L2 cache misses
     * getSecondLevelCachePutCount() = Entities stored into L2 cache
     *
     */
    @ManagedAttribute(description = "Second-level cache hit count")
    public long getSecondLevelCacheHitCount() {
        return stadistics.getSecondLevelCacheHitCount();
    }

    @ManagedAttribute(description = "Second-level cache miss count")
    public long getSecondLevelCacheMissCount() {
        return stadistics.getSecondLevelCacheMissCount();
    }

    @ManagedAttribute(description = "Second-level cache put count")
    public long getSecondLevelCachePutCount() {
        return stadistics.getSecondLevelCachePutCount();
    }

    /**
     * Operations
     * <p>
     * resetStatistics() = Resets all counters to zero
     * enableStatistics() = Starts collecting statistics
     * disableStatistics() = Stops collecting statistics
     * isStatisticsEnabled() = Whether collection is currently active
     *
     */
    @ManagedOperation(description = "Resets all Hibernate statistics counters to zero")
    public void resetStatistics() {
        stadistics.clear();
    }

    @ManagedOperation(description = "Enables statistics collection")
    public void enableStatistics() {
        stadistics.setStatisticsEnabled(true);
    }

    @ManagedOperation(description = "Disables statistics collection")
    public void disableStatistics() {
        stadistics.setStatisticsEnabled(false);
    }

    @ManagedAttribute(description = "Whether statistics collection is active")
    public boolean isStatisticsEnabled() {
        return stadistics.isStatisticsEnabled();
    }


}
