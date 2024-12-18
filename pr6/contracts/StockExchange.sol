// SPDX-License-Identifier: MIT
pragma solidity ^0.8.0;

contract StockExchange {
    struct Stock { //акция
        string name; //название акции
        uint256 price; //цена одной акции
        uint256 totalSupply; //общее кол-во акций
        uint256 availableSupply; //доступное кол-во акций
        address owner; //владелец акции
    }

    mapping(uint256 => Stock) public stocks; // id акции -> акция
    mapping(address => mapping(uint256 => uint256)) public balances; // адрес пользователя -> (id акции -> количество)

    uint256 public stockCount; // кол-во акций

    event StockCreated(uint256 id, string name, uint256 price, uint256 totalSupply); //событие создания акции
    event StockPurchased(address buyer, uint256 stockId, uint256 amount); //событие покупки акции
    event StockTransferred(address from, address to, uint256 stockId, uint256 amount); //событие передачи акции

    // Функция для создания новой акции
    function createStock(string memory _name, uint256 _price, uint256 _totalSupply) public {
        require(_totalSupply > 0, "Total supply must be greater than 0");
        
        stockCount++;
        stocks[stockCount] = Stock(_name, _price, _totalSupply, _totalSupply, msg.sender);
        
        emit StockCreated(stockCount, _name, _price, _totalSupply);
    }

    // Функция для покупки акций
    function purchaseStock(uint256 _stockId, uint256 _amount) public payable {
        Stock storage stock = stocks[_stockId];
        
        require(stock.availableSupply >= _amount, "Not enough stock available"); //Проверяет, достаточно ли акций доступно для покупки.
        require(msg.value >= stock.price * _amount, "Insufficient funds sent"); //Проверяет, достаточно ли средств отправлено

        stock.availableSupply -= _amount;
        balances[msg.sender][_stockId] += _amount;

        emit StockPurchased(msg.sender, _stockId, _amount);
    }

    // Функция для передачи акций
    function transferStock(address _to, uint256 _stockId, uint256 _amount) public {
        require(balances[msg.sender][_stockId] >= _amount, "Not enough stock to transfer"); //Проверяет, достаточно ли акций у отправителя для передачи.

        balances[msg.sender][_stockId] -= _amount;
        balances[_to][_stockId] += _amount;

        emit StockTransferred(msg.sender, _to, _stockId, _amount);
    }
}