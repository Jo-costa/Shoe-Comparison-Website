const mysql = require('mysql2');
const express = require('express');
const app = express();
let cors = require('cors');
const path = require('path');
const { log } = require('console');

let connection = mysql.createConnection({
    host:'localhost',
    user:'root',
    password:'',
    database:'shoes'
})

app.use(cors())
app.use(express.static('public'));
app.use(express.static('js'));
app.set("views", path.resolve(__dirname, "views"));
app.set('view engine', 'ejs');


//define a route to the '/home' endpoint
app.get('/home', (request, response)=>{

    //SQL to retrieve 9 random shoes from the database
    const query = `SELECT models.id, models.*, comparison.*
    FROM models
    INNER JOIN details ON models.id = details.model_id
    INNER JOIN comparison ON details.id = comparison.details_id
    ORDER BY RAND()
    limit 9`


    connection.query(query, (error, result)=>{

        if(error){

            response.status(500).send('Error fetching data')
        }else{

            //render the index page with the retrieved data
            response.render('index', {data: result})
        }

    })
})

//define a route to the '/product/:details_id' endpoint
app.get('/product/:details_id', (request, response)=>{

    //retrieve details_id from the URL
    const details_id = request.params.details_id;
    
    //select the product that corresponds to the details_id
    const query = `SELECT models.*, details.*, comparison.price
    FROM models 
    INNER JOIN details ON models.id = details.model_id
    INNER JOIN comparison ON details.id = comparison.details_id
    WHERE details.id = ${details_id}
    `
    connection.query(query, (error, result)=>{
        if(error) throw error;
        //get the name of the product
        const name = result[0].name;
        //get the name of the retailer name
        const retailer_name = result[0].retailer_name;
        
        //SQL to retrieve products where models.name matches name and details.retailer_name != retailer_name
        const query2 = `SELECT  models.id, models.*,  details.retailer_name, details.retailer_url, comparison.*
        FROM models
        INNER JOIN details ON models.id = details.model_id
        INNER JOIN comparison ON details.id = comparison.details_id
        WHERE models.name like (?)
            AND details.retailer_name != '${retailer_name}'
        GROUP BY details.retailer_name`

        connection.query(query2,[`%${name}%`], (error2, result2)=>{
            if(error2) throw error2;


            console.log(result2);
            //render product page with the retrieved data
            response.render('product', {data: result, data2:result2});
        })

    })
})

//define a route to the '/results' endpoint
app.get('/results', (request, response) => {

    //retrieve the searched entered by user from the URL
    const searchInput = request.query.search;

    //extract the value of the page parameter from the query string or set it to 1 
    //if page parameter not present in the query string
    const page = parseInt(request.query.page) || 1;

    //extract the value of the size parameter from the query string or set it to 9 
    //if size parameter not present in the query string
    const size = parseInt(request.query.size) || 9;
    
    


    //number of items to be retrieved from database
    const limit = size;

    //calculate the number of items to skip based on the current page and the number of products per page
    const skip = (page -1) * size;

    
    //SQl to retrieve the number of products in the models table that matches the searchInput
    const countProducts = `SELECT COUNT(id) FROM models
    WHERE LOWER(models.name) LIKE LOWER(?)
    ` 

    //SQL to retrieve paginated list of products
    const query = `SELECT models.id, models.*, comparison.*
    FROM models
    INNER JOIN details ON models.id = details.model_id
    INNER JOIN comparison ON details.id = comparison.details_id
    WHERE LOWER(models.name) LIKE LOWER(?)
    LIMIT ${limit} OFFSET ${skip}`


    connection.query(countProducts,[`%${searchInput}%`], (err, res) =>{

        if(err) throw err

        connection.query(query,[`%${searchInput}%`], (error, result) =>{
            if(error) throw error;
    
                //check if the first element of res array exists
                //if it exists, totalProducts will be set by res[0]["COUNT(id)"], else it is set to 0
                const totalProducts = res[0] ? res[0]["COUNT(id)"] : 0;

                //Divides totalProducts by size, round it and set totalPages 
                const totalPages = Math.ceil(totalProducts / size);


                console.log(searchInput);
            //render results page with the retrieved data
            response.render('results', {data: result, totalPages: totalPages, currentPage: page, size:size, totalProducts: totalProducts, searchInput:searchInput })
        })

    })
    

})




app.get('/explore', (request, response) => {
    //extract the value of the page parameter from the query string or set it to 1 
    //if page parameter not present in the query string
    const page = parseInt(request.query.page) || 1;

     //extract the value of the size parameter from the query string or set it to 9 
    //if size parameter not present in the query string
    const size = parseInt(request.query.size) || 9;
    
    
    //number of items to be retrieved from database
    const limit = size;
    
    //calculate the number of items to skip based on the current page and the number of products per page
    const skip = (page -1) * size;

    //SQl to retrieve the number of products in the models table
    const countProducts = `SELECT COUNT(id) FROM models` 

    //SQL to retrieve paginated list of products
    const query = `SELECT models.id, models.*, comparison.*
    FROM models
    INNER JOIN details ON models.id = details.model_id
    INNER JOIN comparison ON details.id = comparison.details_id
    LIMIT ${limit} OFFSET ${skip}
    `

    //execute the countProducts query
    connection.query(countProducts, (error, result) =>{

        if(error) throw error;

        //get the total number of products from the datadabase 
        const totalProducts = result[0]["COUNT(id)"];

        //Divides totalProducts by size, round it and set totalPages 
        const totalPages = Math.ceil(totalProducts / size);

        //execute the sql query
        connection.query(query, (err, res) =>{
            if(err) throw err;

            //render explore page with the retrieved data
            response.render('explore',{data: res, totalPages: totalPages, currentPage: page, size:size })

        })

    })
})

const port = 3000

const server = app.listen(port, ()=>{
    console.log("Server running on port "+ port);
})


//export server object from the module
module.exports = server;
