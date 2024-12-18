const { expect } = require("chai");
const { ethers } = require("hardhat");

describe("StockExchange", function () { //Солдунова ИКБО-20-21
  let stockExchange;
  let owner;
  let acc2;

  beforeEach(async function () {
      const StockExchange = await ethers.getContractFactory("StockExchange");
      stockExchange = await StockExchange.deploy();
      await stockExchange.waitForDeployment();
      [owner, acc2] = await ethers.getSigners();
  });

  it("should create a stock", async function () {
      await stockExchange.createStock("Apple", ethers.parseEther("1"), 100);
      
      const stock = await stockExchange.stocks(1);
      expect(stock.name).to.equal("Apple");
      expect(stock.price).to.equal(ethers.parseEther("1"));
      expect(stock.totalSupply).to.equal(100);
      expect(stock.availableSupply).to.equal(100);
  });

  it("should allow buying stocks", async function () {
      await stockExchange.createStock("Google", ethers.parseEther("2"), 50);
      
      await stockExchange.purchaseStock(1, 10, { value: ethers.parseEther("20") });
      
      const stock = await stockExchange.stocks(1);
      expect(stock.availableSupply).to.equal(40); // 50 - 10
  });

  it("should allow selling stocks", async function () {
      await stockExchange.createStock("Amazon", ethers.parseEther("4"), 20);
      await stockExchange.purchaseStock(1, 10, { value: ethers.parseEther("40") });
      await stockExchange.transferStock(acc2.address, 1, 5);
      
      const balance = await stockExchange.balances(acc2.address,1);
      expect(balance).to.equal(5); 
  });
});

//npx hardhat clean
//npx hardhat compile
//npx hardhat test