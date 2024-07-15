// import modules
const chai = require('chai');
const expect = chai.expect;
const chaiHttp = require('chai-http');
// import server file to be tested
const server = require("../js/server.js");

// use chai HTTP plugin
chai.use(chaiHttp);


describe('Server tests', () => {


    // test case to ensure 9 products are returned to the main page
    it('should return 9 random products for the home page route', (done) =>{
        chai.request(server)
        .get('/home')
        .end((err, res)=>{
            // assertion response is expected to be an object
            expect(res).to.be.a('Object');
            done();

        });
    })

    it('should return the product with the specified details_id', (done) =>{
        chai.request(server)
        .get('/product/2050')
        .end((err, res)=>{
            expect(res).to.be.an('Object');
            done();

        });
    })

    it('should return the first 9 products in the DB', (done) =>{
        chai.request(server)
        .get('/explore')
        .end((err, res)=>{
            expect(res).to.be.an('Object');
            done();
        })
    })

    it('should return the second page with a limit of 9 items', (done) =>{
        chai.request(server)
        .get('/explore?page=2&limit=9')
        .end((err, res)=>{
            expect(res).to.be.an('Object');
            done();
        })
    })

    it('should return all products related to the search', (done) =>{
        chai.request(server)
        .get('/results?search=dunk')
        .end((err, res)=>{
            expect(res).to.be.an('Object');
            done();
        })
    })

    


})
