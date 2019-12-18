const token = process.argv[2] ? process.argv[2] : 'PI';
const cluster = require('cluster');
const cpus = require('os').cpus();
const cpu = cpus[0];
const numCPUs = cpus.length;
const pid = process.pid;

const redisHost = 'xxxx.redislabs.com';
const redisPort = '12345';

const redisConfig = {host: redisHost, port: redisPort};
const jobQueueKey = 'job#' + token;

const resultsQueueKey = 'results#' + token;

if (cluster.isMaster)
{
    runManager();
}
else
{
    runWorker();
}

function runManager()
{
    console.log(`Active node ${pid} on ${cpu.model}, ${cpu.speed} MHz, ${numCPUs} logical CPUs for "${token}" processing`);
    for (var i = 0; i < numCPUs; i++){
        cluster.fork();
    }
    cluster.on('online', (worker, message) =>
    {
        console.log(`worker.process.pid} is running.`);
    });
    cluster.on('exit', (worker)=>
    {
        console.log(`worker ${worker.process.pid} exited; forking new one.`);
        cluster.fork();
    });
    cluster.on('message', (worker, message) =>
    {
        if (message.error){
            console.log(`worker ${worker.process.pid} error: ${message.error}`);
        }
        else if (message.jobResult)
        {
            const result = message.jobResult;
            console.log(`job result: id=${result.id}, value=${result.value}`);
        }
    });
}
function runWorker()
{
    const redisClient = require('redis').createClient(redisConfig);
    redisClient.on('error', (err) =>
    {
        process.send({error: err});
    });
    function doWork(jobItem)
    {
        const N = jobItem.count;
        return{
            id: jobItem.id,
            value: {count: N, estimation: calcMonteCarloPi(N)}
        };
    }
    function getJob(err, res)
    {
        const job = JSON.parse(res[1]);
        const result = doWork(job);
        process.send
        ({
            jobResult:
            {
                id: result.id,
                value: result.estimation
            }
        });
        redisClient.lpush
        (
            resultsQueueKey,
            JSON.stringify(result)
        );
        redisClien.brpop(jobQueueKey, 0, getJob);
    }
    redisClient.brpop(jobQueueKey, 0, getJob);
}
