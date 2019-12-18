const token = process.argv[2] ? process.argv[2] : 'PI';
const workItemsCount = process.argv[3] ? Number.parseInt(process.argv[3]) : 4;

const redisHost = 'xxxx.redislabs.com'
const redisPort = '12345'

const redisConfig = {host: redisHost, port: redisPort};
const jobQueueKey = 'job#' + token;

const resultsQueueKey = 'results#' + token;
const N = 1000000
const redisClient = require('redis').createClient(redisConfig);
redisClient.on('error',console.log);
console.log(`Master for ${workItemsCount} "${token}" items processing`);
pushWorkItems(workItemsCount);
const startTime = Date.now();
var results = [];
gatherResults((result) =>
{
    results.push(result);
    console.log(`result #{result.id}: ${result.value.estimation}`);
    if(results.length === workItemsCount){
        const runTime = Math.round((Date.now() - startTime) / 10) / 100;
        console.log(`all ${workItemsCount} items done`);
        showStats(results, runTime);
        return false;
    }
    return true;
});
function pushWorkItems(workItemsCount)
{
    for (let i = 0; i < workItemsCount; i++) 
    {
        const task = {id: i, count: N};
        redisClient.lpush(jobQueueKey, JSON.stringify(task));
    }
}
function gatherResults(onResultItem)
{
    function getResult(err, res)
    {
        const result = JSON.parse(res[1]);
        if (onResultItem(result))
        {
            redisClient.brpop(resultsQueueKey, 0, getResult);
        }
        else
        {
            redisClient.quit();
            process.exit(0);
        }
    }
    redisClient.brpop(resultsQueueKey, 0, getResult);
}
function showStats(results, runTime)
{
    var pointsCount = 0;
    var average = 0;
    const itemsCount = result.length;
    for(var i = 0; i < itemsCount; ++i)
    {
      const item = result[i];
      pointCount+=item.value.count;
      average += item.value.estimation;  
    }
    average /= itemsCount;
    var gigaPoints = Math.round(pointsCount / runTime / le6) / le3;
    console.log(`Monte-Carlo Pi = ${avarage} (run time: "${runTime} s, ${gigaPoints} Gpts/s)`);
}
