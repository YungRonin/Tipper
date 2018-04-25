pragma solidity ^0.4.18;

interface token {
    function transfer(address receiver, uint amount);
    function transferFrom(address sender, address reciever, uint _value);
}

contract Escrow {
    address public escrow;
    uint public alphaTokenPrice;
    uint public betaTokenPrice;
    token public alphaToken;
    token public betaToken;
    token public omegaToken;

    function Escrow(
        address omegaTokenAddress,
        address alphaTokenAddress,
        address betaTokenAddress
    ) {
        alphaToken = token(alphaTokenAddress);
        betaToken = token(betaTokenAddress);
        omegaToken = token(omegaTokenAddress);

    }

    function sendOmega(uint256 _value) public {
      require(alphaToken.transferFrom(msg.sender, address(this), _value / alphaTokenPrice));
      require(betaToken.transferFrom(msg.sender, address(this), _value / betaTokenPrice));
      omegaToken.transfer(msg.sender, _value)
    }

    function receiveOmega(uint256 _value) public {
      require(omegaToken.transferFrom(msg.sender, address(this), _value));
      alphaToken.transfer(msg.sender, _value / alphaTokenPrice);
      betaToken.transfer(msg.sender, _value / betaTokenPrice);
    }
}
